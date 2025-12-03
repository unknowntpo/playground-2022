#!/bin/bash
# Startup script for Flink blog trending posts example

set -e

echo "=================================================="
echo "Flink Blog Trending Posts - Startup Script"
echo "=================================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check if docker-compose services are running
print_info "Checking PostgreSQL..."
if ! docker-compose ps --services --filter "status=running" | grep -q postgres; then
    print_info "Starting PostgreSQL..."
    docker-compose up -d postgres redis
    print_info "Waiting for PostgreSQL to be ready..."
    sleep 5
else
    print_success "PostgreSQL is already running"
fi

# Check PostgreSQL readiness
POSTGRES_CONTAINER=$(docker-compose ps -q postgres)
if [ -n "$POSTGRES_CONTAINER" ] && docker exec $POSTGRES_CONTAINER pg_isready -U flinkuser -d blogdb > /dev/null 2>&1; then
    print_success "PostgreSQL is ready"
else
    print_warning "PostgreSQL is not ready yet. Please wait..."
    exit 1
fi

# Check which component to run
case "${1:-all}" in
    docker)
        print_info "Checking for required JAR files..."
        if [ ! -d "lib" ] || [ -z "$(ls -A lib 2>/dev/null)" ]; then
            print_info "JARs not found, downloading..."
            ./download-jars.sh
        else
            print_success "JARs already exist in lib/"
        fi

        print_info "Building and starting containerized Flink application..."
        docker-compose up --build -d pyflink-app

        print_success "Flink application container started!"
        echo ""
        echo "Services running:"
        echo "  - PostgreSQL:  localhost:15432"
        echo "  - Redis:       localhost:16379"
        echo "  - Flink WebUI: http://localhost:18081"
        echo ""
        echo "To view logs:"
        echo "  ./run.sh docker-logs"
        echo ""
        echo "To stop:"
        echo "  docker-compose down"
        echo ""
        echo "To start load generator (from host):"
        echo "  ./run.sh load-generator"
        ;;

    docker-logs)
        print_info "Tailing Flink application logs (Ctrl+C to exit)..."
        docker-compose logs -f pyflink-app
        ;;

    load-generator)
        print_info "Starting load generator..."
        uv run python load_generator.py --rate ${2:-10}
        ;;

    flink-cdc)
        print_info "Starting Flink CDC -> Redis pipeline..."
        uv run python flink_cdc_simple.py
        ;;

    flink)
        print_info "Starting Flink pipeline (print mode)..."
        uv run python flink_pipeline.py
        ;;

    dashboard)
        print_info "Starting Streamlit dashboard (Redis mode)..."
        uv run streamlit run dashboard_redis.py
        ;;

    dashboard-pg)
        print_info "Starting Streamlit dashboard (PostgreSQL mode)..."
        uv run streamlit run dashboard.py
        ;;

    all)
        print_info "Starting all services with Docker Compose..."

        # Check for JARs
        if [ ! -d "lib" ] || [ -z "$(ls -A lib 2>/dev/null)" ]; then
            print_info "JARs not found, downloading..."
            ./download-jars.sh
        else
            print_success "JARs already exist in lib/"
        fi

        # Start all services with docker-compose
        print_info "Building and starting all containers..."
        docker-compose up --build -d

        print_success "All services started!"
        echo ""
        echo "Services running:"
        echo "  - PostgreSQL:  localhost:15432"
        echo "  - Redis:       localhost:16379"
        echo "  - Flink WebUI: http://localhost:18081"
        echo ""
        echo "To start load generator:"
        echo "  ./run.sh load-generator [rate]"
        echo ""
        echo "To view Flink logs:"
        echo "  ./run.sh docker-logs"
        echo ""
        echo "To stop all services:"
        echo "  docker-compose down"
        echo ""
        ;;

    stop-all)
        print_info "Stopping all background services..."

        # Stop load generator
        if [ -f logs/load_generator.pid ]; then
            LOAD_PID=$(cat logs/load_generator.pid)
            if kill -0 $LOAD_PID 2>/dev/null; then
                kill $LOAD_PID
                print_success "Load generator stopped (PID: $LOAD_PID)"
            else
                print_warning "Load generator not running"
            fi
            rm -f logs/load_generator.pid
        fi

        # Stop Flink CDC
        if [ -f logs/flink_cdc.pid ]; then
            FLINK_PID=$(cat logs/flink_cdc.pid)
            if kill -0 $FLINK_PID 2>/dev/null; then
                kill $FLINK_PID
                print_success "Flink CDC stopped (PID: $FLINK_PID)"
            else
                print_warning "Flink CDC not running"
            fi
            rm -f logs/flink_cdc.pid
        fi

        # Stop any remaining python processes
        pkill -f "load_generator.py" 2>/dev/null || true
        pkill -f "flink_cdc_simple.py" 2>/dev/null || true

        print_success "All background services stopped"
        ;;

    stop)
        print_info "Stopping Docker services..."
        docker-compose down
        print_success "Docker services stopped"

        print_info "To stop background Python processes, run: ./run.sh stop-all"
        ;;

    status)
        print_info "Service Status:"
        echo ""

        # Docker services
        echo "Docker Services:"
        docker-compose ps

        echo ""
        echo "Background Python Services:"

        # Load generator
        if [ -f logs/load_generator.pid ]; then
            LOAD_PID=$(cat logs/load_generator.pid)
            if kill -0 $LOAD_PID 2>/dev/null; then
                echo "  ✓ Load Generator: Running (PID: $LOAD_PID)"
            else
                echo "  ✗ Load Generator: Not running"
            fi
        else
            echo "  - Load Generator: Not started"
        fi

        # Flink CDC
        if [ -f logs/flink_cdc.pid ]; then
            FLINK_PID=$(cat logs/flink_cdc.pid)
            if kill -0 $FLINK_PID 2>/dev/null; then
                echo "  ✓ Flink CDC: Running (PID: $FLINK_PID)"
            else
                echo "  ✗ Flink CDC: Not running"
            fi
        else
            echo "  - Flink CDC: Not started"
        fi

        echo ""
        echo "Logs available in: logs/"
        ;;

    logs)
        if [ -z "$2" ]; then
            print_info "Tailing all logs (Ctrl+C to exit)..."
            tail -f logs/*.log 2>/dev/null || echo "No log files found"
        else
            print_info "Tailing $2 log (Ctrl+C to exit)..."
            tail -f logs/$2.log 2>/dev/null || echo "Log file not found: logs/$2.log"
        fi
        ;;

    *)
        echo "Usage: $0 {docker|docker-logs|load-generator|flink-cdc|flink|dashboard|dashboard-pg|all|stop|stop-all|status|logs}"
        echo ""
        echo "Docker Commands (Recommended - with WebUI):"
        echo "  all                    - Start everything with Docker Compose (Postgres + Redis + Flink)"
        echo "  docker                 - Start only Flink app container (WebUI: http://localhost:18081)"
        echo "  docker-logs            - Tail Flink container logs"
        echo ""
        echo "Local Commands (Development - no WebUI):"
        echo "  load-generator [rate]  - Start load generator with optional rate (default: 10)"
        echo "  flink-cdc              - Start Flink CDC pipeline (local Python)"
        echo "  dashboard              - Start Streamlit dashboard (Redis mode)"
        echo "  dashboard-pg           - Start Streamlit dashboard (PostgreSQL mode)"
        echo ""
        echo "Management Commands:"
        echo "  stop                   - Stop Docker services"
        echo "  stop-all               - Stop all background Python services"
        echo "  status                 - Show status of all services"
        echo "  logs [service]         - Tail logs (all or specific: load_generator, flink_cdc)"
        exit 1
        ;;
esac
