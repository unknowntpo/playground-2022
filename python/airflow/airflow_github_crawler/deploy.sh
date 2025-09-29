#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
check_docker() {
    log_info "Checking Docker status..."
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker is not running. Please start Docker and try again."
        exit 1
    fi
    log_success "Docker is running"
}

# Check if Docker Compose is available
check_docker_compose() {
    log_info "Checking Docker Compose availability..."
    if command -v docker-compose > /dev/null 2>&1; then
        DOCKER_COMPOSE="docker-compose"
    elif docker compose version > /dev/null 2>&1; then
        DOCKER_COMPOSE="docker compose"
    else
        log_error "Docker Compose is not available"
        exit 1
    fi
    log_success "Docker Compose available: $DOCKER_COMPOSE"
}

# Start MinIO service
start_minio() {
    log_info "Starting MinIO service..."
    $DOCKER_COMPOSE -f docker-compose.services.yml up -d minio minio-init

    # Wait for MinIO to be healthy
    log_info "Waiting for MinIO to be healthy..."
    timeout 60 bash -c 'until docker-compose -f docker-compose.services.yml ps minio | grep -q "healthy"; do sleep 2; done' || {
        log_error "MinIO failed to start properly"
        exit 1
    }

    log_success "MinIO is running at http://localhost:9001 (admin: minioadmin/minioadmin123)"
}

# Start Airflow services
start_airflow() {
    log_info "Starting Airflow services..."

    # Initialize Airflow if needed
    if ! $DOCKER_COMPOSE -f docker-compose.airflow.yml ps airflow-init | grep -q "exited (0)"; then
        log_info "Initializing Airflow database..."
        $DOCKER_COMPOSE -f docker-compose.airflow.yml up airflow-init
    fi

    # Start Airflow services
    $DOCKER_COMPOSE -f docker-compose.airflow.yml up -d

    # Wait for Airflow webserver to be healthy
    log_info "Waiting for Airflow to be ready..."
    timeout 120 bash -c 'until curl -f http://localhost:8080/api/v2/monitor/health > /dev/null 2>&1; do sleep 5; done' || {
        log_warning "Airflow may still be starting up. Check http://localhost:8080"
    }

    log_success "Airflow is running at http://localhost:8080 (admin: airflow/airflow)"
}

# Stop all services
stop_services() {
    log_info "Stopping all services..."
    $DOCKER_COMPOSE -f docker-compose.airflow.yml down || true
    $DOCKER_COMPOSE -f docker-compose.services.yml down || true
    log_success "All services stopped"
}

# Clean up everything (including volumes)
cleanup() {
    log_warning "This will remove all data including MinIO storage and Airflow database!"
    read -p "Are you sure you want to continue? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        log_info "Cleaning up all services and volumes..."
        $DOCKER_COMPOSE -f docker-compose.airflow.yml down -v --remove-orphans || true
        $DOCKER_COMPOSE -f docker-compose.services.yml down -v --remove-orphans || true

        # Remove custom networks
        docker network rm airflow_github_crawler_default 2>/dev/null || true
        docker network rm airflow_github_crawler_github-crawler 2>/dev/null || true

        log_success "Cleanup completed"
    else
        log_info "Cleanup cancelled"
    fi
}

# Show service status
status() {
    log_info "Service Status:"
    echo

    # MinIO Services - check if actually running
    minio_running=$(docker ps --filter "name=minio" --filter "status=running" --format "table {{.Names}}" | grep -v NAMES | wc -l)
    if [ "$minio_running" -gt 0 ]; then
        echo "=== MinIO Services ==="
        docker ps --filter "name=minio" --filter "status=running" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        echo
    fi

    # Airflow Services - check if actually running
    airflow_running=$(docker ps --filter "name=airflow" --filter "status=running" --format "table {{.Names}}" | grep -v NAMES | wc -l)
    if [ "$airflow_running" -gt 0 ]; then
        echo "=== Airflow Services ==="
        echo "📊 API Server:     $(docker ps --format '{{.Status}}' --filter name=apiserver --filter status=running | head -1)"
        echo "📅 Scheduler:      $(docker ps --format '{{.Status}}' --filter name=scheduler --filter status=running | head -1)"
        echo "⚙️  Worker:        $(docker ps --format '{{.Status}}' --filter name=worker --filter status=running | head -1)"
        echo "🗄️  Database:      $(docker ps --format '{{.Status}}' --filter name=postgres --filter status=running | head -1)"
        echo
    fi

    if [ "$minio_running" -eq 0 ] && [ "$airflow_running" -eq 0 ]; then
        echo "No services are currently running."
        echo
    fi

    # Check service endpoints
    log_info "Service Endpoints:"

    if curl -s http://localhost:9001 > /dev/null 2>&1; then
        echo "✅ MinIO Console:  http://localhost:9001 (minioadmin/minioadmin123)"
    else
        echo "❌ MinIO Console:  http://localhost:9001 (not running)"
    fi

    if curl -s http://localhost:8080/api/v2/monitor/health > /dev/null 2>&1; then
        echo "✅ Airflow Web UI: http://localhost:8080 (airflow/airflow)"
    else
        echo "❌ Airflow Web UI: http://localhost:8080 (not ready)"
    fi
}

# Show logs
logs() {
    service=${2:-}
    if [[ -n "$service" ]]; then
        if [[ "$service" == "minio" ]]; then
            $DOCKER_COMPOSE -f docker-compose.services.yml logs -f "$service"
        else
            $DOCKER_COMPOSE -f docker-compose.airflow.yml logs -f "$service"
        fi
    else
        log_info "Available services to view logs:"
        echo "  MinIO: minio"
        echo "  Airflow: airflow-webserver, airflow-scheduler, airflow-worker, airflow-apiserver"
        echo ""
        echo "Usage: $0 logs <service-name>"
    fi
}

# Trigger DAG manually
trigger_dag() {
    log_info "Triggering GitHub trending crawler DAG..."

    if ! curl -f http://localhost:8080/api/v2/monitor/health > /dev/null 2>&1; then
        log_error "Airflow is not running or not accessible"
        exit 1
    fi

    # Trigger via docker exec
    $DOCKER_COMPOSE -f docker-compose.airflow.yml exec airflow-apiserver airflow dags trigger github_trending_crawler
    log_success "DAG triggered successfully"
    log_info "Check the Airflow UI at http://localhost:8080 to monitor progress"
}

# Main script logic
case "${1:-}" in
    start)
        check_docker
        check_docker_compose
        start_minio
        start_airflow
        log_success "All services started successfully!"
        echo
        status
        ;;
    start-airflow)
        check_docker
        check_docker_compose
        start_airflow
        log_success "Airflow services started successfully!"
        echo
        status
        ;;
    start-minio)
        check_docker
        check_docker_compose
        start_minio
        log_success "MinIO services started successfully!"
        echo
        status
        ;;
    stop)
        check_docker_compose
        stop_services
        ;;
    restart)
        check_docker_compose
        stop_services
        sleep 2
        start_minio
        start_airflow
        log_success "All services restarted successfully!"
        ;;
    status)
        check_docker_compose
        status
        ;;
    logs)
        check_docker_compose
        logs "$@"
        ;;
    trigger)
        check_docker_compose
        trigger_dag
        ;;
    cleanup)
        check_docker_compose
        cleanup
        ;;
    *)
        echo "GitHub Trending Crawler - Deployment Manager"
        echo
        echo "Usage: $0 {start|start-airflow|start-minio|stop|restart|status|logs|trigger|cleanup}"
        echo
        echo "Commands:"
        echo "  start         - Start all services (MinIO + Airflow)"
        echo "  start-airflow - Start only Airflow services"
        echo "  start-minio   - Start only MinIO services"
        echo "  stop          - Stop all services"
        echo "  restart       - Restart all services"
        echo "  status        - Show status of all services"
        echo "  logs          - Show logs for a specific service"
        echo "  trigger       - Manually trigger the GitHub crawler DAG"
        echo "  cleanup       - Stop and remove all services and data (destructive!)"
        echo
        echo "Examples:"
        echo "  $0 start"
        echo "  $0 logs airflow-scheduler"
        echo "  $0 trigger"
        echo "  $0 status"
        exit 1
        ;;
esac