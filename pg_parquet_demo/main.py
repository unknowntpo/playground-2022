#!/usr/bin/env python3
"""
Query pg_parquet demo database
"""

import psycopg2
from psycopg2.extras import RealDictCursor


def main():
    # Database connection parameters
    conn_params = {
        'host': 'localhost',
        'port': 5435,
        'database': 'demo',
        'user': 'postgres',
        'password': 'postgres'
    }

    conn = None
    for i in range(5):
        try:
            # Connect to database
            if i == 0:
                print("Connecting to PostgreSQL...")
            else:
                print(f"Retrying connection ({i+1}/5)...")
            
            conn = psycopg2.connect(**conn_params)
            break
        except psycopg2.Error:
            if i == 4:
                raise
            import time
            time.sleep(2)

    try:
        cursor = conn.cursor(cursor_factory=RealDictCursor)

        # Query 1: Check pg_parquet extension
        print("\n=== Checking pg_parquet extension ===")
        cursor.execute("SELECT * FROM pg_extension WHERE extname = 'pg_parquet';")
        result = cursor.fetchone()
        print(f"Extension installed: {result['extname']}")

        # Query 2: List all tables
        print("\n=== Available tables ===")
        cursor.execute("""
            SELECT tablename, tableowner
            FROM pg_tables
            WHERE schemaname = 'public'
            ORDER BY tablename;
        """)
        for row in cursor.fetchall():
            print(f"  - {row['tablename']} (owner: {row['tableowner']})")

        # Query 3: Query regular table
        print("\n=== Sales data (regular table) ===")
        cursor.execute("SELECT * FROM sales_data LIMIT 5;")
        print(f"Total rows: {cursor.rowcount}")
        for row in cursor.fetchall():
            print(f"  {row['product_name']:20} | {row['category']:15} | ${row['price']:8.2f} | {row['region']}")

        # Query 4: Query Imported Parquet table
        print("\n=== Sales data (imported from parquet) ===")
        cursor.execute("SELECT * FROM sales_parquet_import LIMIT 5;")
        print(f"Total rows: {cursor.rowcount}")
        for row in cursor.fetchall():
            print(f"  {row['product_name']:20} | {row['category']:15} | ${row['price']:8.2f} | {row['region']}")

        # Query 5: Inspect Parquet Schema
        print("\n=== Inspect Parquet Schema ===")
        try:
            cursor.execute("SELECT * FROM parquet.schema('/tmp/sales_data.parquet');")
            print(f"{'Field':<20} | {'Type':<15}")
            print("-" * 40)
            for row in cursor.fetchall():
                if row['name']: # Skip schema root
                    print(f"{row['name']:<20} | {row['type_name'] or 'STRUCT/LIST':<15}")
        except psycopg2.Error as e:
            print(f"Skipping schema inspection due to error: {e}")
            conn.rollback()


        # Query 6: Inspect Parquet Metadata
        print("\n=== Inspect Parquet Metadata ===")
        try:
            cursor.execute("SELECT * FROM parquet.metadata('/tmp/sales_data.parquet');")
            meta = cursor.fetchone()
            if meta:
                print(f"File: {meta['uri']}")
                print(f"Row Groups: {meta['row_group_id'] + 1} (showing last)")
                print(f"Total Bytes: {meta['row_group_bytes']}")
        except psycopg2.Error as e:
            print(f"Skipping metadata inspection due to error: {e}")
            conn.rollback()

        cursor.close()
        conn.close()
        print("\n✓ Demo completed successfully!")

    except psycopg2.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"Error: {e}")


if __name__ == "__main__":
    main()
