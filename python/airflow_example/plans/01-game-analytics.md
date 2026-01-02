# Game Player Event Pipeline

## Problem
- Battle Royale game player stats
- Events: kills, deaths per player
- Need: top 10 players by KD ratio (daily)

## Concepts
- **TaskFlow API**: `@task` decorator (Airflow 3.x)
- **Sensor**: Wait for new parquet file in S3
- **DuckDB**: Query parquet files directly
- **Polars**: DataFrame aggregation
- **Parquet**: Columnar storage (partitioned by date)

## Flow
```
                         DAG 1: Producer
Kafka (in-mem fake) → aggregate by date → S3 parquet
                                              │
                                              ▼
                               s3://bucket/events/2025_12_30_events.parquet
                               s3://bucket/events/2025_12_31_events.parquet
                                              │
                         DAG 2: Consumer      ▼
                      Sensor (detect new file)
                                              │
                                              ▼
                      DuckDB query parquet → top 10 KD → results parquet
```

## S3 Structure
```
s3://game-analytics/
  └── events/
      ├── 2025_12_30_events.parquet
      ├── 2025_12_31_events.parquet
      └── ...
  └── leaderboard/
      ├── 2025_12_30_top10.parquet
      └── ...
```

## Input (Kafka / in-mem fake)
```python
events = [
    {"player_id": "p123", "event_type": "KILL", "game_id": "g001", "ts": "2025-12-30T10:00:00"},
    {"player_id": "p123", "event_type": "DEATH", "game_id": "g001", "ts": "2025-12-30T10:01:00"},
    ...
]
```

## Output (top 10 leaderboard)
```
| rank | player_id | kills | deaths | kd_ratio |
|------|-----------|-------|--------|----------|
| 1    | p123      | 50    | 10     | 5.00     |
| 2    | p456      | 30    | 15     | 2.00     |
```

## DAG 1: Event Producer (aggregate raw events to daily parquet)
```python
from airflow.sdk import dag, task
from datetime import datetime

@dag(
    dag_id="game_event_producer",
    start_date=datetime(2024, 1, 1),
    schedule="@daily",
    catchup=False,
    tags=["game", "producer"],
)
def game_event_producer():

    @task
    def consume_events() -> list[dict]:
        # Fake Kafka - replace with real consumer later
        events = [
            {"player_id": "p123", "event_type": "KILL", "game_id": "g001"},
            {"player_id": "p123", "event_type": "KILL", "game_id": "g001"},
            {"player_id": "p123", "event_type": "DEATH", "game_id": "g001"},
            {"player_id": "p456", "event_type": "KILL", "game_id": "g001"},
            {"player_id": "p456", "event_type": "DEATH", "game_id": "g001"},
            {"player_id": "p456", "event_type": "DEATH", "game_id": "g001"},
        ]
        return events

    @task
    def aggregate_to_parquet(events: list[dict], date_str: str) -> str:
        import polars as pl
        import boto3

        df = pl.DataFrame(events)
        stats = df.group_by("player_id").agg([
            (pl.col("event_type") == "KILL").sum().alias("kills"),
            (pl.col("event_type") == "DEATH").sum().alias("deaths"),
        ])

        # Write to S3
        path = f"/tmp/{date_str}_events.parquet"
        stats.write_parquet(path)

        s3 = boto3.client("s3", endpoint_url="http://localstack:4566")
        s3.upload_file(path, "game-analytics", f"events/{date_str}_events.parquet")
        return f"s3://game-analytics/events/{date_str}_events.parquet"

    date_str = "{{ ds_nodash }}"  # e.g., 20251230
    events = consume_events()
    aggregate_to_parquet(events, date_str)

game_event_producer()
```

## DAG 2: Leaderboard Consumer (sensor + query)
```python
from airflow.sdk import dag, task
from airflow.sensors.s3 import S3KeySensor
from datetime import datetime

@dag(
    dag_id="game_leaderboard",
    start_date=datetime(2024, 1, 1),
    schedule=None,  # Triggered by sensor
    catchup=False,
    tags=["game", "consumer"],
)
def game_leaderboard():

    wait_for_file = S3KeySensor(
        task_id="wait_for_events",
        bucket_name="game-analytics",
        bucket_key="events/{{ ds_nodash }}_events.parquet",
        aws_conn_id="aws_localstack",
    )

    @task
    def query_top_10(date_str: str) -> str:
        import duckdb

        conn = duckdb.connect(":memory:")
        conn.execute("INSTALL httpfs; LOAD httpfs;")
        conn.execute("SET s3_endpoint='localstack:4566'; SET s3_use_ssl=false;")

        result = conn.execute(f"""
            SELECT player_id, kills, deaths,
                   kills::float / deaths as kd_ratio
            FROM read_parquet('s3://game-analytics/events/{date_str}_events.parquet')
            ORDER BY kd_ratio DESC
            LIMIT 10
        """).fetchdf()

        # Write results
        out_path = f"/tmp/{date_str}_top10.parquet"
        result.to_parquet(out_path)
        return out_path

    @task
    def upload_results(local_path: str, date_str: str):
        import boto3
        s3 = boto3.client("s3", endpoint_url="http://localstack:4566")
        s3.upload_file(local_path, "game-analytics", f"leaderboard/{date_str}_top10.parquet")

    date_str = "{{ ds_nodash }}"
    path = query_top_10(date_str)
    wait_for_file >> path
    upload_results(path, date_str)

game_leaderboard()
```

## Future: Real Kafka
```python
from confluent_kafka import Consumer

def consume_events():
    consumer = Consumer({"bootstrap.servers": "kafka:9092", "group.id": "game-stats"})
    consumer.subscribe(["game-events"])
    events = []
    for msg in consumer.consume(num_messages=10000, timeout=30):
        events.append(json.loads(msg.value()))
    return events
```

## Dependencies
- duckdb
- polars
- boto3
- confluent-kafka (future)
