# Flink Blog Trending Posts Project

## Overview

This project is a real-time analytics application that demonstrates a complete Change Data Capture (CDC) pipeline. It simulates a blog platform where user "view events" are ingested, processed in real-time to calculate trending posts, and visualized on a live dashboard.

**Core Architecture:**
1.  **Data Source:** A **Load Generator** (`load_generator.py`) writes simulated view events to **PostgreSQL**.
2.  **Ingestion:** **Apache Flink** (via PyFlink) captures these inserts in real-time using Flink CDC (`postgres-cdc` connector).
3.  **Processing:** Flink aggregates views into **10-second tumbling windows** (not 1-minute as stated in some docs) and calculates the Top 10 posts.
4.  **Storage:** Aggregated results are written back to a specific table in **PostgreSQL**.
5.  **Caching:** A background Python thread (running alongside the Flink job) syncs the latest results from PostgreSQL to **Redis** for low-latency access.
6.  **Visualization:** A **Streamlit** dashboard (`dashboard.py`) reads from Redis and updates the UI in real-time.

## Tech Stack

*   **Language:** Python 3.12+ (managed by `uv`)
*   **Stream Processing:** Apache Flink 1.20 / PyFlink
*   **Database:** PostgreSQL 16 (with logical replication enabled)
*   **Cache:** Redis 7
*   **Frontend:** Streamlit
*   **Infrastructure:** Docker & Docker Compose

## Key Files

*   `flink_cdc_simple.py`: The main application entry point. Contains the Flink CDC logic, window aggregation queries, and the background Redis sync thread.
*   `load_generator.py`: Simulates user traffic using a power-law distribution (Zipf's law) to mimic realistic "viral" content.
*   `dashboard.py`: The Streamlit frontend application.
*   `run.sh`: The central control script for starting/stopping services and managing logs.
*   `docker-compose.yml`: Defines the PostgreSQL and Redis services. Note that Flink runs as a local process connecting to these containers in the default "hybrid" setup, though a `pyflink-app` container is also defined.

## Setup & Usage

**Prerequisites:** Docker, Python 3.12+, `uv` (Python package manager).

### Quick Start (Recommended)

Use the `run.sh` wrapper to launch the full stack:

```bash
./run.sh all
```
This starts Postgres/Redis (Docker), downloads necessary Flink JARs, installs Python deps, and launches the generator, pipeline, and dashboard.

### Manual Commands

*   **Start Infrastructure:** `docker-compose up -d postgres redis`
*   **Install Dependencies:** `uv sync` and `./download-jars.sh`
*   **Run Flink Job:** `./run.sh flink-cdc`
*   **Run Generator:** `./run.sh load-generator 10` (10 events/sec)
*   **Run Dashboard:** `./run.sh dashboard`
*   **View Logs:** `./run.sh logs`
*   **Check Status:** `./run.sh status`
*   **Stop All:** `./run.sh stop-all` (stops Python scripts) then `./run.sh stop` (stops Docker)

## Development Notes

*   **JAR Dependencies:** Flink requires specific JARs (CDC, JDBC, Postgres Driver) located in the `lib/` directory. The `download-jars.sh` script handles this.
*   **CDC Configuration:** Postgres is configured with `wal_level=logical`. The Flink job uses the `pgoutput` plugin.
*   **Hybrid Sync Pattern:** Unlike typical pipelines that write directly to Redis from Flink, this project writes to Postgres first, then "relays" to Redis via a Python thread in `flink_cdc_simple.py`. This allows inspecting results in SQL while keeping the dashboard fast.
*   **Discrepancy:** The code (`flink_cdc_simple.py`) uses a **10-second** window, while `README.md` may reference a 1-minute window. The code is the source of truth.
