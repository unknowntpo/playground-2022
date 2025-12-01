#!/bin/bash
set -e

echo "Starting pg_parquet demo..."

# Clean up previous runs
echo "Cleaning up previous runs..."
docker-compose down -v

# Start Docker containers
echo "1. Building and starting PostgreSQL container..."
echo "   Note: The first build will take several minutes to compile pg_parquet."
docker-compose up -d --build

# Wait for PostgreSQL to be ready
echo "2. Waiting for PostgreSQL to be ready..."
until docker exec pg_parquet_demo pg_isready -U postgres > /dev/null 2>&1; do
    echo "   Waiting for PostgreSQL..."
    sleep 2
done

echo "3. PostgreSQL is ready!"
echo "4. Running Python query script..."
echo ""

/opt/homebrew/bin/uv run python main.py
