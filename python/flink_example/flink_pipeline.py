#!/usr/bin/env python3
"""
Flink CDC pipeline for tracking trending blog posts.
Captures view events from PostgreSQL via CDC and computes top 10 trending posts.
"""
import os
from pyflink.table import EnvironmentSettings, TableEnvironment
from pyflink.table.expressions import col, lit
from pyflink.table.window import Tumble

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

def create_sink_table(t_env: TableEnvironment):
    """Create print sink for output."""
    sink_ddl = """
        CREATE TABLE trending_posts_sink (
            window_start TIMESTAMP(3),
            window_end TIMESTAMP(3),
            post_id INT,
            title VARCHAR,
            view_count BIGINT
        ) WITH (
            'connector' = 'print'
        )
    """
    t_env.execute_sql(sink_ddl)
    print("[INFO] Created sink table: trending_posts_sink")

def compute_trending_posts(t_env: TableEnvironment):
    """Compute top 10 trending posts using window aggregation."""
    # Aggregate view counts per post in 5-minute windows
    aggregated = t_env.sql_query("""
        SELECT
            TUMBLE_START(viewed_at, INTERVAL '5' MINUTE) as window_start,
            TUMBLE_END(viewed_at, INTERVAL '5' MINUTE) as window_end,
            post_id,
            COUNT(*) as view_count
        FROM view_events_source
        GROUP BY
            TUMBLE(viewed_at, INTERVAL '5' MINUTE),
            post_id
    """)

    t_env.create_temporary_view("aggregated_views", aggregated)

    # Join with posts to get titles and select top 10
    top_posts = t_env.sql_query("""
        SELECT
            a.window_start,
            a.window_end,
            a.post_id,
            p.title,
            a.view_count
        FROM aggregated_views a
        LEFT JOIN posts_lookup FOR SYSTEM_TIME AS OF a.window_end AS p
        ON a.post_id = p.id
        ORDER BY a.window_start DESC, a.view_count DESC
    """)

    return top_posts

def main():
    """Main entry point for Flink pipeline."""
    print("[INFO] Starting Flink CDC pipeline...")

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
    t_env.get_config().set("execution.checkpointing.interval", "60s")

    # Create tables
    create_cdc_source_table(t_env)
    create_posts_lookup_table(t_env)
    create_sink_table(t_env)

    # Compute trending posts
    trending = compute_trending_posts(t_env)

    # Insert results into sink
    trending.execute_insert("trending_posts_sink").wait()

if __name__ == '__main__':
    main()
