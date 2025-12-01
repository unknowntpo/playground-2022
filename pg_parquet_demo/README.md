# pg_parquet Demo

Demo setup for PostgreSQL with pg_parquet extension.

## Setup

1. Start PostgreSQL with pg_parquet:
   ```bash
   docker-compose up -d
   ```

2. Wait for initialization (first run takes longer due to pg_parquet compilation):
   ```bash
   docker-compose logs -f postgres
   ```

3. Run Python query script:
   ```bash
   uv run main.py
   ```

## Components

- **docker-compose.yml**: Postgres 16 on port 5435
- **init-scripts/01-install-pg_parquet.sh**: Installs pg_parquet extension
- **init-scripts/02-create-extension.sql**: Creates extension and test data
- **main.py**: Python script to query parquet tables

## Test Data

Two parquet tables created:
- `sales_parquet`: Sales transaction data
- `sales_summary`: Aggregated sales by category/region

## Cleanup

```bash
docker-compose down -v
```
