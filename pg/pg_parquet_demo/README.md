# pg_parquet Benchmark Demo

This project demonstrates the performance and storage benefits of using [pg_parquet](https://github.com/CrunchyData/pg_parquet) with PostgreSQL.

It compares three storage formats:
1.  **Postgres Heap Table**: Standard database table (Hot Data).
2.  **Parquet File**: Columnar compressed file (Cold/Archive Data).
3.  **CSV File**: Row-based text file (Interchange Format).

## Benchmark Results (Example)

On a dataset of **2,000,000 rows**, typical results show:

| Metric | Postgres Table (Hot) | Parquet File (Cold) | CSV File (Cold) | Winner |
| :--- | :--- | :--- | :--- | :--- |
| **Storage Size** | ~236 MB | **~135 MB** | ~215 MB | **Parquet (43% Smaller)** |
| **Full Read Speed** | **~0.06s** (RAM) | ~3.5s | ~3.2s | **Postgres** |
| **Column Read** | ~0.06s | **~1.6s** | ~3.2s | **Parquet** (vs CSV) |

### Key Takeaways

*   **Postgres Tables** are optimized for speed (RAM/Caching) but take up the most space.
*   **Parquet** is ideal for **Archival** and **Data Lakes**. It is significantly smaller than CSV and Postgres tables.
*   **Columnar Access**: Reading a single column from Parquet is **2x faster** than reading from CSV (which requires scanning the whole file).
*   `pg_parquet` enables efficient moving of data between these tiers (DB <-> Parquet).

## How to Run

1.  **Run the automated benchmark script:**
    ```bash
    ./run.sh
    ```
    This script will:
    - Build the Docker container with `pg_parquet`.
    - Start PostgreSQL.
    - Run the Python benchmark (`main.py`).
    - Clean up afterwards.

## Project Structure

- `main.py`: The benchmark script. Generates data, runs tests, and prints a report.
- `docker-compose.yml`: Postgres configuration with `pg_parquet` installed.
- `Dockerfile`: Builds Postgres 16 with `pg_parquet` from source.
- `init-scripts/`: SQL scripts for initial setup.
- `tmp_data/`: Directory for benchmark files (mounted to container).

## Requirements

- Docker & Docker Compose
- Python 3
- `uv` (optional) or `pip install psycopg2-binary`
