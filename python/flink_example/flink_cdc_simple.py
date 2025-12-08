#!/usr/bin/env python3
"""
Simplified Flink CDC pipeline with Redis sink.
Uses Table API with print sink and separate Redis updater.
"""
import os
import json
import redis
import threading
import time
from datetime import datetime
from pyflink.table import EnvironmentSettings, TableEnvironment

# Redis configuration
REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", "16379"))
REDIS_KEY_TOP_POSTS = "trending:top10"
REDIS_KEY_WINDOW_STATS = "trending:window_stats"

# PostgreSQL configuration
POSTGRES_HOST = os.getenv("POSTGRES_HOST", "localhost")
POSTGRES_PORT = int(os.getenv("POSTGRES_PORT", "15432"))

# Connect to Redis
redis_client = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)


def create_cdc_source(t_env: TableEnvironment):
    """Create CDC source for view_events."""
    ddl = f"""
        CREATE TABLE view_events_source (
            id BIGINT,
            post_id INT,
            user_id INT,
            viewed_at TIMESTAMP(3),
            WATERMARK FOR viewed_at AS viewed_at - INTERVAL '5' SECOND
        ) WITH (
            'connector' = 'postgres-cdc',
            'hostname' = '{POSTGRES_HOST}',
            'port' = '{POSTGRES_PORT}',
            'username' = 'flinkuser',
            'password' = 'flinkpass',
            'database-name' = 'blogdb',
            'schema-name' = 'public',
            'table-name' = 'view_events',
            'slot.name' = 'flink_slot',
            'decoding.plugin.name' = 'pgoutput'
        )
    """
    t_env.execute_sql(ddl)
    print("[✓] CDC source created")


def create_posts_lookup(t_env: TableEnvironment):
    """Create posts lookup table using CDC for continuous updates."""
    ddl = f"""
        CREATE TABLE posts_lookup (
            id INT PRIMARY KEY NOT ENFORCED,
            title VARCHAR,
            content VARCHAR,
            created_at TIMESTAMP(3)
        ) WITH (
            'connector' = 'postgres-cdc',
            'hostname' = '{POSTGRES_HOST}',
            'port' = '{POSTGRES_PORT}',
            'username' = 'flinkuser',
            'password' = 'flinkpass',
            'database-name' = 'blogdb',
            'schema-name' = 'public',
            'table-name' = 'posts',
            'slot.name' = 'flink_posts_slot',
            'decoding.plugin.name' = 'pgoutput'
        
)
    """
    t_env.execute_sql(ddl)
    print("[✓] Posts lookup created")


def create_result_sink(t_env: TableEnvironment):
    """Create sink for top posts results."""
    print("[DEBUG] Preparing sink DDL...", flush=True)
    ddl = f"""
        CREATE TABLE top_posts_sink (
            window_end TIMESTAMP(3),
            post_id INT,
            title STRING,
            view_count BIGINT,
            rank_num BIGINT,
            PRIMARY KEY (window_end, post_id) NOT ENFORCED
        ) WITH (
            'connector' = 'jdbc',
            'url' = 'jdbc:postgresql://{POSTGRES_HOST}:{POSTGRES_PORT}/blogdb',
            'table-name' = 'trending_posts',
            'username' = 'flinkuser',
            'password' = 'flinkpass'
        )
    """
    print(
        "[DEBUG] Executing sink DDL (this may take a moment to validate JDBC connection)...",
        flush=True,
    )
    t_env.execute_sql(ddl)
    print("[✓] Results sink created", flush=True)


def compute_top_posts(t_env: TableEnvironment):
    """Compute and insert top 10 trending posts."""
    insert_sql = """
        INSERT INTO top_posts_sink
        SELECT
            window_end,
            post_id,
            CONCAT('Post ', CAST(post_id AS STRING)) as title,
            view_count,
            rank_num
        FROM (
            SELECT
                window_end,
                post_id,
                view_count,
                ROW_NUMBER() OVER (
                    PARTITION BY window_end
                    ORDER BY view_count DESC
                ) as rank_num
            FROM (
                SELECT
                    TUMBLE_END(viewed_at, INTERVAL '10' SECOND) as window_end,
                    post_id,
                    COUNT(*) as view_count
                FROM view_events_source
                GROUP BY
                    TUMBLE(viewed_at, INTERVAL '10' SECOND),
                    post_id
            )
        )
        WHERE rank_num <= 10
    """
    return t_env.execute_sql(insert_sql)


def redis_updater():
    """Background thread to sync trending_posts table to Redis."""
    import psycopg2

    print("[INFO] Redis updater thread started")

    conn = psycopg2.connect(
        host=POSTGRES_HOST,
        port=POSTGRES_PORT,
        database="blogdb",
        user="flinkuser",
        password="flinkpass",
    )

    while True:
        try:
            cursor = conn.cursor()

            # Get latest window results
            cursor.execute(
                """
                SELECT window_end, post_id, title, view_count, rank_num
                FROM trending_posts
                WHERE window_end = (SELECT MAX(window_end) FROM trending_posts)
                ORDER BY rank_num
                LIMIT 10
            """
            )

            rows = cursor.fetchall()
            cursor.close()

            if rows:
                # Clear old data
                redis_client.delete(REDIS_KEY_TOP_POSTS)

                # Write to Redis
                for window_end, post_id, title, view_count, rank in rows:
                    result = {
                        "rank": int(rank),
                        "post_id": int(post_id),
                        "title": str(title),
                        "view_count": int(view_count),
                        "window_end": str(window_end),
                        "updated_at": datetime.now().isoformat(),
                    }

                    redis_client.zadd(
                        REDIS_KEY_TOP_POSTS, {json.dumps(result): -int(view_count)}
                    )

                # Update window stats
                window_stats = {
                    "window_end": str(rows[0][0]),
                    "updated_at": datetime.now().isoformat(),
                    "total_posts": len(rows),
                }
                redis_client.set(REDIS_KEY_WINDOW_STATS, json.dumps(window_stats))

                print(f"[REDIS] Updated top {len(rows)} posts (window: {rows[0][0]})")

            time.sleep(5)  # Update every 5 seconds

        except Exception as e:
            print(f"[REDIS ERROR] {e}")
            time.sleep(10)


def main():
    """Main pipeline."""
    print("=" * 80, flush=True)
    print("Flink CDC -> PostgreSQL -> Redis Pipeline", flush=True)
    print("=" * 80, flush=True)
    print(f"Redis: {REDIS_HOST}:{REDIS_PORT}", flush=True)
    print(f"Window: 1-minute tumbling", flush=True)
    print(f"Top-K: 10 posts", flush=True)
    print("=" * 80, flush=True)

    # Test Redis
    try:
        redis_client.ping()
        print("[✓] Redis connected\n", flush=True)
    except:
        print("[✗] Redis connection failed!", flush=True)
        return

    # Setup Flink
    print("[DEBUG] Creating environment settings...", flush=True)
    env_settings = EnvironmentSettings.in_streaming_mode()
    print("[DEBUG] Creating table environment...", flush=True)
    t_env = TableEnvironment.create(env_settings)
    print("[DEBUG] Table environment created!", flush=True)

    # Add JARs
    lib_dir = os.path.dirname(__file__)
    cdc_jar = os.path.join(lib_dir, "lib/flink-sql-connector-postgres-cdc-3.3.0.jar")
    jdbc_connector_jar = os.path.join(
        lib_dir, "lib/flink-connector-jdbc-3.2.0-1.19.jar"
    )
    pg_driver_jar = os.path.join(lib_dir, "lib/postgresql-42.7.1.jar")

    if (
        not os.path.exists(cdc_jar)
        or not os.path.exists(jdbc_connector_jar)
        or not os.path.exists(pg_driver_jar)
    ):
        print(f"[✗] JARs not found in {lib_dir}/lib/")
        return

    jars = f"file://{os.path.abspath(cdc_jar)};file://{os.path.abspath(jdbc_connector_jar)};file://{os.path.abspath(pg_driver_jar)}"
    print(f"[DEBUG] Setting JAR configuration...", flush=True)
    t_env.get_config().set("pipeline.jars", jars)
    t_env.get_config().set("execution.checkpointing.interval", "30s")
    print(f"[✓] JARs loaded\n", flush=True)

    # Create tables
    print(f"[DEBUG] Connecting to PostgreSQL...", flush=True)
    import psycopg2

    conn = psycopg2.connect(
        host=POSTGRES_HOST,
        port=POSTGRES_PORT,
        database="blogdb",
        user="flinkuser",
        password="flinkpass",
    )
    cursor = conn.cursor()
    print(f"[DEBUG] Creating results table...", flush=True)
    cursor.execute(
        """
        CREATE TABLE IF NOT EXISTS trending_posts (
            window_end TIMESTAMP,
            post_id INTEGER,
            title VARCHAR(255),
            view_count BIGINT,
            rank_num BIGINT,
            PRIMARY KEY (window_end, post_id)
        )
    """
    )
    conn.commit()
    cursor.close()
    conn.close()
    print("[✓] Results table created in PostgreSQL\n", flush=True)

    print("[DEBUG] Creating CDC source...", flush=True)
    create_cdc_source(t_env)
    # create_posts_lookup(t_env)  # Temporarily disabled to simplify
    print("[DEBUG] Creating result sink...", flush=True)
    create_result_sink(t_env)
    print("[DEBUG] Sink created, starting Redis thread...", flush=True)

    # Start Redis updater thread
    updater_thread = threading.Thread(target=redis_updater, daemon=True)
    updater_thread.start()
    print("[DEBUG] Redis thread started", flush=True)

    print("\n[INFO] Starting Flink pipeline...", flush=True)
    print("[INFO] CDC events will be aggregated and synced to Redis", flush=True)
    print("[INFO] Press Ctrl+C to stop\n", flush=True)

    # Execute
    print("[DEBUG] Calling compute_top_posts...", flush=True)
    result = compute_top_posts(t_env)
    print("[DEBUG] Waiting for pipeline completion...", flush=True)
    result.wait()


if __name__ == "__main__":
    main()
