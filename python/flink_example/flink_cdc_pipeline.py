#!/usr/bin/env python3
"""
Flink CDC pipeline for computing trending blog posts.
Captures view events via CDC, computes top 10 in windows, and writes to Redis.
"""
import os
import json
import redis as redis_module
from datetime import datetime
from pyflink.table import EnvironmentSettings, TableEnvironment
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.functions import SinkFunction
from pyflink.common import Types

# Redis configuration
REDIS_HOST = 'localhost'
REDIS_PORT = 16379
REDIS_KEY_TOP_POSTS = 'trending:top10'
REDIS_KEY_WINDOW_STATS = 'trending:window_stats'

def create_cdc_source_table(t_env: TableEnvironment):
    """Create CDC source table for view_events."""
    source_ddl = """
        CREATE TABLE view_events_source (
            id BIGINT,
            post_id INT,
            user_id INT,
            viewed_at TIMESTAMP(3),
            WATERMARK FOR viewed_at AS viewed_at - INTERVAL '5' SECOND
        ) WITH (
            'connector' = 'postgres-cdc',
            'hostname' = 'localhost',
            'port' = '15432',
            'username' = 'flinkuser',
            'password' = 'flinkpass',
            'database-name' = 'blogdb',
            'schema-name' = 'public',
            'table-name' = 'view_events',
            'slot.name' = 'flink_slot',
            'decoding.plugin.name' = 'pgoutput'
        )
    """
    t_env.execute_sql(source_ddl)
    print("[INFO] Created CDC source table: view_events_source")

def create_posts_lookup_table(t_env: TableEnvironment):
    """Create lookup table for posts."""
    lookup_ddl = """
        CREATE TABLE posts_lookup (
            id INT PRIMARY KEY NOT ENFORCED,
            title VARCHAR,
            content VARCHAR,
            created_at TIMESTAMP(3)
        ) WITH (
            'connector' = 'jdbc',
            'url' = 'jdbc:postgresql://localhost:15432/blogdb',
            'table-name' = 'posts',
            'username' = 'flinkuser',
            'password' = 'flinkpass'
        )
    """
    t_env.execute_sql(lookup_ddl)
    print("[INFO] Created lookup table: posts_lookup")

def create_print_sink_table(t_env: TableEnvironment):
    """Create print sink for debugging."""
    sink_ddl = """
        CREATE TABLE trending_results (
            window_start TIMESTAMP(3),
            window_end TIMESTAMP(3),
            post_id INT,
            title STRING,
            view_count BIGINT,
            rank_num BIGINT
        ) WITH (
            'connector' = 'print'
        )
    """
    t_env.execute_sql(sink_ddl)
    print("[INFO] Created print sink table: trending_results")

def compute_and_sink_top_posts(t_env: TableEnvironment):
    """Compute top 10 trending posts and write to Redis."""

    # Step 1: Aggregate view counts per post in 1-minute tumbling windows
    aggregated = t_env.sql_query("""
        SELECT
            TUMBLE_START(viewed_at, INTERVAL '1' MINUTE) as window_start,
            TUMBLE_END(viewed_at, INTERVAL '1' MINUTE) as window_end,
            post_id,
            COUNT(*) as view_count
        FROM view_events_source
        GROUP BY
            TUMBLE(viewed_at, INTERVAL '1' MINUTE),
            post_id
    """)

    t_env.create_temporary_view("aggregated_views", aggregated)

    # Step 2: Join with posts to get titles
    with_titles = t_env.sql_query("""
        SELECT
            a.window_start,
            a.window_end,
            a.post_id,
            p.title,
            a.view_count
        FROM aggregated_views a
        LEFT JOIN posts_lookup FOR SYSTEM_TIME AS OF a.window_end AS p
        ON a.post_id = p.id
    """)

    t_env.create_temporary_view("posts_with_titles", with_titles)

    # Step 3: Rank posts within each window (top 10)
    top_posts = t_env.sql_query("""
        SELECT
            window_start,
            window_end,
            post_id,
            title,
            view_count,
            ROW_NUMBER() OVER (
                PARTITION BY window_start
                ORDER BY view_count DESC
            ) as rank_num
        FROM posts_with_titles
    """)

    t_env.create_temporary_view("ranked_posts", top_posts)

    # Step 4: Filter top 10 and write to Redis
    final_top10 = t_env.sql_query("""
        SELECT
            window_start,
            window_end,
            post_id,
            title,
            view_count,
            rank_num
        FROM ranked_posts
        WHERE rank_num <= 10
        ORDER BY window_start DESC, rank_num ASC
    """)

    return final_top10

class RedisSinkFunction:
    """Custom sink function to write results to Redis."""

    def __init__(self):
        self.redis_client = None

    def open(self):
        """Initialize Redis connection."""
        self.redis_client = redis.Redis(
            host=REDIS_HOST,
            port=REDIS_PORT,
            decode_responses=True
        )
        print(f"[INFO] Connected to Redis at {REDIS_HOST}:{REDIS_PORT}")

    def process_element(self, row):
        """Process each row and write to Redis."""
        if not self.redis_client:
            self.open()

        window_start = row[0]
        window_end = row[1]
        post_id = row[2]
        title = row[3]
        view_count = row[4]
        rank = row[5]

        # Create result record
        result = {
            'rank': int(rank),
            'post_id': int(post_id),
            'title': str(title) if title else f"Post {post_id}",
            'view_count': int(view_count),
            'window_start': str(window_start),
            'window_end': str(window_end),
            'updated_at': datetime.now().isoformat()
        }

        # Store in Redis as sorted set (for easy top-k retrieval)
        # Use window_start as part of key to keep recent windows
        window_key = f"trending:{window_start.strftime('%Y%m%d_%H%M')}"

        # Add to sorted set (score = -view_count for descending order)
        self.redis_client.zadd(
            window_key,
            {json.dumps(result): -int(view_count)}
        )

        # Also update the latest top 10
        if rank <= 10:
            # Store as list in latest key
            self.redis_client.zadd(
                REDIS_KEY_TOP_POSTS,
                {json.dumps(result): -int(view_count)}
            )

        # Keep only top 10 in latest
        self.redis_client.zremrangebyrank(REDIS_KEY_TOP_POSTS, 10, -1)

        # Store window metadata
        window_stats = {
            'window_start': str(window_start),
            'window_end': str(window_end),
            'updated_at': datetime.now().isoformat()
        }
        self.redis_client.set(REDIS_KEY_WINDOW_STATS, json.dumps(window_stats))

        # Set expiry on window-specific keys (keep last 24 hours)
        self.redis_client.expire(window_key, 86400)

        print(f"[REDIS] Rank #{rank}: {title} ({view_count} views) -> {window_key}")

    def close(self):
        """Close Redis connection."""
        if self.redis_client:
            self.redis_client.close()

def main():
    """Main entry point for Flink CDC pipeline."""
    print("[INFO] Starting Flink CDC -> Redis pipeline...")
    print(f"[INFO] Redis: {REDIS_HOST}:{REDIS_PORT}")
    print(f"[INFO] Window: 1-minute tumbling windows")
    print(f"[INFO] Output: Top 10 trending posts\n")

    # Setup environment
    env_settings = EnvironmentSettings.in_streaming_mode()
    t_env = TableEnvironment.create(env_settings)

    # Add CDC connector and JDBC driver JARs
    lib_dir = os.path.join(os.path.dirname(__file__), "lib")
    cdc_jar = os.path.join(lib_dir, "flink-sql-connector-postgres-cdc-3.3.0.jar")
    jdbc_jar = os.path.join(lib_dir, "postgresql-42.7.1.jar")

    if not os.path.exists(cdc_jar):
        raise FileNotFoundError(f"CDC connector JAR not found: {cdc_jar}")
    if not os.path.exists(jdbc_jar):
        raise FileNotFoundError(f"JDBC driver JAR not found: {jdbc_jar}")

    jars = f"file://{os.path.abspath(cdc_jar)};file://{os.path.abspath(jdbc_jar)}"
    t_env.get_config().set("pipeline.jars", jars)
    print(f"[INFO] Added JARs: {cdc_jar}, {jdbc_jar}")

    # Configure checkpoint
    t_env.get_config().set("execution.checkpointing.interval", "30s")

    # Create tables
    create_cdc_source_table(t_env)
    create_posts_lookup_table(t_env)
    create_redis_sink_table(t_env)

    # Compute top posts
    top_posts = compute_and_sink_top_posts(t_env)

    # Convert to DataStream and apply Redis sink
    stream_env = StreamExecutionEnvironment.get_execution_environment()
    ds = t_env.to_changelog_stream(top_posts)

    # Process each record and write to Redis
    redis_sink = RedisSinkFunction()

    def process_and_sink(row):
        redis_sink.process_element(row)
        return row

    # Map and execute
    ds.map(process_and_sink).print()

    # Execute
    print("\n[INFO] Pipeline started. Processing CDC events...")
    print("[INFO] Press Ctrl+C to stop\n")
    stream_env.execute("Flink CDC to Redis Pipeline")

if __name__ == '__main__':
    main()
