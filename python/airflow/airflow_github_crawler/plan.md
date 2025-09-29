# Airflow Github Crawler

Use Airflow to periodically get top 10 trending GitHub repository specified by date.
Default is today, and save it at minio.

## Architecture
- FastAPI for Backend.
- Streamlit to display board.
- Airflow for periodically crawling and manual triggered crawling.
- Minio for storing data.
- docker compose file to deploy locally.
- GCP cloud run to deploy to cloud run.

## Spec:
Should follow TDD to deploy this project.
And should pass e2e locally. and then go to GCP cloud run.

---

# Detailed Implementation Plan

## Phase 1: Project Setup & Dependencies
- [ ] Update pyproject.toml with all required dependencies (airflow, fastapi, streamlit, minio, etc.)
- [ ] Create proper project directory structure (/src, /tests, /docker)
- [ ] Set up development environment and virtual environment
- [ ] Initialize git repository with proper .gitignore

## Phase 2: Core Services (TDD Approach)
- [ ] **GitHub Service**
  - [ ] Write tests for GitHub API integration
  - [ ] Implement GitHub trending repositories fetcher
  - [ ] Handle rate limiting and error cases
  - [ ] Add configuration for date ranges and repository count
- [ ] **MinIO Service**
  - [ ] Write tests for MinIO operations (upload/download)
  - [ ] Implement data storage service
  - [ ] Handle connection errors and retries
  - [ ] Add data versioning support
- [ ] **Data Models**
  - [ ] Define Repository model (name, stars, language, description, etc.)
  - [ ] Define storage format (JSON/Parquet)
  - [ ] Add data validation schemas

## Phase 3: Airflow Integration
- [ ] **Custom Airflow Operators**
  - [ ] Write tests for custom operators
  - [ ] Implement GitHubTrendingOperator
  - [ ] Implement MinIOUploadOperator
  - [ ] Implement DataValidationOperator
- [ ] **Airflow DAG**
  - [ ] Create daily scheduled DAG for trending repos
  - [ ] Add manual trigger capability
  - [ ] Implement error handling and retries
  - [ ] Add data quality checks

## Phase 4: FastAPI Backend
- [ ] **API Endpoints**
  - [ ] Write API tests (unit and integration)
  - [ ] GET /repositories - fetch stored data with filters
  - [ ] POST /trigger - manual DAG trigger
  - [ ] GET /health - health check endpoint
  - [ ] GET /repositories/dates - available data dates
- [ ] **Integration Layer**
  - [ ] Connect to MinIO for data retrieval
  - [ ] Connect to Airflow for DAG management
  - [ ] Add authentication if needed
  - [ ] Implement proper error handling and logging

## Phase 5: Streamlit Dashboard
- [ ] **Dashboard Components**
  - [ ] Write UI component tests
  - [ ] Create main dashboard layout
  - [ ] Implement repository list display with sorting/filtering
  - [ ] Add date range selection widget
  - [ ] Create manual trigger button with status feedback
  - [ ] Add repository details modal/page
- [ ] **Data Visualization**
  - [ ] Add trending charts (stars over time)
  - [ ] Language distribution charts
  - [ ] Repository statistics dashboard

## Phase 6: Docker & Local Deployment
- [ ] **Docker Configuration**
  - [ ] Create Dockerfile for each service
  - [ ] Create docker-compose.yml with all services:
    - [ ] Airflow (webserver, scheduler, worker)
    - [ ] MinIO service
    - [ ] FastAPI service
    - [ ] Streamlit service
    - [ ] PostgreSQL for Airflow metadata
- [ ] **Environment Configuration**
  - [ ] Create .env files for different environments
  - [ ] Add proper volume mounts for data persistence
  - [ ] Configure networking between services

## Phase 7: Testing & Quality Assurance
- [ ] **Unit Tests**
  - [ ] Achieve >80% test coverage for all services
  - [ ] Add property-based testing where applicable
  - [ ] Mock external dependencies (GitHub API, MinIO)
- [ ] **Integration Tests**
  - [ ] Test API endpoints with real database
  - [ ] Test Airflow DAG execution
  - [ ] Test service-to-service communication
- [ ] **End-to-End Tests**
  - [ ] Full workflow testing (trigger → crawl → store → display)
  - [ ] UI testing with Streamlit
  - [ ] Performance testing under load

## Phase 8: GCP Cloud Run Deployment
- [ ] **Cloud Preparation**
  - [ ] Containerize services for Cloud Run
  - [ ] Configure Cloud SQL for Airflow metadata
  - [ ] Set up Cloud Storage as MinIO alternative
  - [ ] Configure IAM and service accounts
- [ ] **Deployment**
  - [ ] Create Cloud Run services
  - [ ] Set up Cloud Scheduler for DAG triggers
  - [ ] Configure monitoring and logging
  - [ ] Add health checks and auto-scaling

## Phase 9: Documentation & Maintenance
- [ ] **Documentation**
  - [ ] API documentation with OpenAPI/Swagger
  - [ ] Deployment instructions
  - [ ] Architecture diagrams
  - [ ] Troubleshooting guide
- [ ] **Monitoring**
  - [ ] Add application metrics
  - [ ] Set up alerts for failures
  - [ ] Add performance monitoring

---

## Project Structure
```
airflow-github-crawler/
├── src/
│   ├── airflow/
│   │   ├── dags/
│   │   ├── operators/
│   │   └── plugins/
│   ├── api/
│   │   ├── routers/
│   │   └── models/
│   ├── streamlit/
│   │   └── pages/
│   └── services/
│       ├── github_service.py
│       ├── minio_service.py
│       └── models.py
├── tests/
│   ├── unit/
│   ├── integration/
│   └── e2e/
├── docker/
│   ├── airflow/
│   ├── api/
│   └── streamlit/
├── docker-compose.yml
└── pyproject.toml
```