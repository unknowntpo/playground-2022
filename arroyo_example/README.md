# Arroyo vs Bytewax (Flink Style) Demo

This project replicates the [Bytewax Flink Replacement Demo](../bytewax_example/) using **Arroyo** (a Rust-based streaming SQL engine).

It demonstrates how to implement the same event-time windowing and aggregation logic using standard SQL in Arroyo.

## What this Demo Does

It processes the same simulated stream of user events:
1.  **Ingest**: Reads events from a JSONL file (`data/events.jsonl`) which acts as our source.
2.  **Key & Window**: Groups data by `user_id` and applies a **5-second Tumbling Window**.
3.  **Aggregate**: Counts the number of events per user per window.

## Comparison: Bytewax vs Arroyo

| Feature | Bytewax (Python) | Arroyo (SQL) |
| :--- | :--- | :--- |
| **Paradigm** | Dataflow Code (Imperative/Functional) | Declarative SQL |
| **Source** | `op.input(..., TestingSource)` | `CREATE TABLE ... WITH (connector='filesystem', ...)` |
| **Keying** | `op.key_on(..., lambda e: e['user_id'])` | `GROUP BY user_id, ...` |
| **Windowing** | `TumblingWindower(...)` | `TUMBLE(interval '5 seconds')` |
| **Logic** | Python Functions (`count_events`) | SQL Aggregates (`count(*)`) |

## Prerequisites

*   Docker
*   Docker Compose

## How to Run

1.  **Start Arroyo Local Cluster**:
    ```bash
    docker compose up -d
    ```
    This starts a single-node Arroyo cluster and mounts the local `data` directory.

2.  **Open Arroyo Console**:
    Visit [http://localhost:5115](http://localhost:5115) in your browser.

3.  **Create Pipeline**:
    *   Click on **"Pipelines"** -> **"Create Pipeline"**.
    *   Copy the content of `pipeline.sql` and paste it into the SQL editor.
    *   Click **"Start Preview"** or **"Deploy"**.

4.  **View Results**:
    *   If running a **Preview**, you will see the results appearing in the bottom panel.
    *   **Expected Output** (similar to Bytewax):
        *   `user_1`: 3 events in the first window.
        *   `user_2`: 2 events in the first window.
        *   `user_1`: 1 event in the second window.

## Clean Up

To stop the cluster:
```bash
docker compose down
```
