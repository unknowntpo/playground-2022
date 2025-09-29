# GitHub Trending Crawler - Deployment Guide

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Airflow       │    │     MinIO       │    │   GitHub API    │
│   ┌─────────┐   │    │   (S3 Storage)  │    │                 │
│   │   DAG   │──────→ │                 │    │                 │
│   └─────────┘   │    │  ┌───────────┐  │    │                 │
│   ┌─────────┐   │    │  │  Bucket   │  │    │                 │
│   │Scheduler│   │    │  │ github-   │  │    │                 │
│   └─────────┘   │    │  │ trending  │  │    │                 │
│   ┌─────────┐   │    │  └───────────┘  │    │                 │
│   │ Worker  │   │    │                 │    │                 │
│   └─────────┘   │    └─────────────────┘    └─────────────────┘
└─────────────────┘
```

## Quick Start

### 1. Start All Services
```bash
./deploy.sh start
```

This will:
- Start MinIO (S3-compatible storage)
- Initialize MinIO bucket
- Start Airflow services
- Initialize Airflow database

### 2. Access Services

**MinIO Console**: http://localhost:9001
- Username: `minioadmin`
- Password: `minioadmin123`

**Airflow Web UI**: http://localhost:8080
- Username: `airflow`
- Password: `airflow`

### 3. Trigger the DAG
```bash
./deploy.sh trigger
```

Or manually in Airflow UI:
1. Go to http://localhost:8080
2. Find "github_trending_crawler" DAG
3. Toggle it ON
4. Click the play button to trigger

## Deployment Commands

### Service Management
```bash
# Start all services
./deploy.sh start

# Stop all services
./deploy.sh stop

# Restart all services
./deploy.sh restart

# Check service status
./deploy.sh status
```

### Monitoring & Debugging
```bash
# View logs for specific service
./deploy.sh logs airflow-scheduler
./deploy.sh logs minio

# Trigger DAG manually
./deploy.sh trigger
```

### Data Management
```bash
# Clean up everything (DESTRUCTIVE!)
./deploy.sh cleanup
```

## File Structure

```
airflow-github-crawler/
├── docker-compose.airflow.yml      # Airflow services
├── docker-compose.services.yml     # Our services (MinIO, API, etc.)
├── deploy.sh                       # Deployment management script
├── dags/
│   ├── github_trending_dag.py      # Main DAG
│   └── services/
│       ├── github_service.py       # GitHub scraping
│       └── s3_service.py           # S3/MinIO storage
└── src/
    └── services/                   # Source services
```

## Data Storage

### Local Storage (Backup)
- Path: `/tmp/github_trending/`
- Format: `trending_YYYY-MM-DD.json`

### MinIO S3 Storage (Primary)
- Bucket: `github-trending`
- Path: `trending/YYYY-MM-DD/repositories.json`
- Format: Enhanced JSON with metadata

### Data Format
```json
{
  "repositories": [
    {
      "name": "owner/repo-name",
      "url": "https://github.com/owner/repo-name",
      "description": "Repository description",
      "language": "Python",
      "stars": 12345,
      "forks": 678,
      "stars_today": 89,
      "crawled_at": "2024-01-01T12:00:00Z",
      "trending_date": "2024-01-01"
    }
  ],
  "count": 10,
  "execution_date": "2024-01-01",
  "crawled_at": "2024-01-01T12:00:00Z",
  "source": "github_trending_crawler"
}
```

## Configuration

### Environment Variables (.env)
```
AIRFLOW_UID=50000
_PIP_ADDITIONAL_REQUIREMENTS=requests beautifulsoup4 lxml boto3 minio
```

### MinIO Configuration
- Endpoint: `localhost:9000` (API), `localhost:9001` (Console)
- Access Key: `minioadmin`
- Secret Key: `minioadmin123`
- Bucket: `github-trending`

## Troubleshooting

### Services Won't Start
```bash
# Check Docker is running
docker info

# Check service status
./deploy.sh status

# View logs
./deploy.sh logs airflow-scheduler
```

### DAG Not Loading
1. Check logs: `./deploy.sh logs airflow-dag-processor`
2. Verify DAG syntax: Check Airflow UI > DAGs > Import Errors
3. Restart services: `./deploy.sh restart`

### MinIO Connection Issues
1. Check MinIO is healthy: `./deploy.sh status`
2. Verify bucket exists: Login to http://localhost:9001
3. Check network connectivity between services

### Data Not Appearing in S3
1. Check DAG execution logs in Airflow UI
2. Verify MinIO credentials in DAG
3. Check S3 service initialization logs

## Production Deployment

For production deployment:

1. **Use external databases**: Replace SQLite with PostgreSQL
2. **Use cloud storage**: Replace MinIO with AWS S3/GCS/Azure Blob
3. **Enable authentication**: Configure proper Airflow authentication
4. **Monitor resources**: Set up monitoring and alerting
5. **Backup strategies**: Implement proper backup procedures

## Next Steps

1. **Add FastAPI service**: Enable `api` profile in docker-compose.services.yml
2. **Add Streamlit dashboard**: Enable `dashboard` profile
3. **Deploy to cloud**: Use cloud-native services
4. **Add monitoring**: Implement metrics and alerting