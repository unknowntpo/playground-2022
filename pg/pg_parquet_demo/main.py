#!/usr/bin/env python3
"""
Benchmark pg_parquet extension: Parquet vs CSV
"""

import psycopg2
from psycopg2.extras import RealDictCursor
import time
import os

# Configuration
DATA_ROWS = 2000000
CSV_FILE_PG = '/tmp_data/bench.csv'
PARQUET_FILE_PG = '/tmp_data/bench.parquet'
CSV_FILE_LOCAL = 'tmp_data/bench.csv'
PARQUET_FILE_LOCAL = 'tmp_data/bench.parquet'

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
            if i == 0:
                print("Connecting to PostgreSQL...")
            else:
                print(f"Retrying connection ({i+1}/5)...")
            
            conn = psycopg2.connect(**conn_params)
            break
        except psycopg2.Error:
            if i == 4:
                raise
            time.sleep(2)
    
    conn.autocommit = True # Important for some operations or just easier handling
    
    try:
        cursor = conn.cursor(cursor_factory=RealDictCursor)

        print(f"\n=== Setting up Benchmark Data ({DATA_ROWS:,} rows) ===")
        
        # 1. Create Benchmark Table
        cursor.execute("DROP TABLE IF EXISTS benchmark_data;")
        cursor.execute("""
            CREATE TABLE benchmark_data (
                id SERIAL PRIMARY KEY,
                uuid_col UUID,
                int_col INTEGER,
                float_col DOUBLE PRECISION,
                text_col TEXT,
                date_col DATE
            );
        """)
        
        # 2. Populate Data
        start_time = time.time()
        cursor.execute(f"""
            INSERT INTO benchmark_data (uuid_col, int_col, float_col, text_col, date_col)
            SELECT 
                gen_random_uuid(), 
                (random() * 100000)::int, 
                random() * 100000, 
                md5(random()::text), 
                CURRENT_DATE - (random() * 365)::int
            FROM generate_series(1, {DATA_ROWS});
        """)
        duration = time.time() - start_time
        print(f"Generated {DATA_ROWS:,} rows in {duration:.2f}s")

        # 3. Export Benchmark
        print("\n=== Export Benchmark (Creating Files) ===")
        
        # Export CSV
        print("Exporting to CSV...")
        start_time = time.time()
        cursor.execute(f"COPY benchmark_data TO '{CSV_FILE_PG}' (FORMAT CSV);")
        csv_export_time = time.time() - start_time
        print(f"CSV Export Time:     {csv_export_time:.4f}s")

        # Export Parquet
        print("Exporting to Parquet...")
        start_time = time.time()
        cursor.execute(f"COPY benchmark_data TO '{PARQUET_FILE_PG}' (FORMAT 'parquet');")
        parquet_export_time = time.time() - start_time
        print(f"Parquet Export Time: {parquet_export_time:.4f}s")

        # 4. Storage Analysis
        print("\n=== Storage Space Analysis ===")
        
        # Get Postgres Table Size
        cursor.execute("SELECT pg_total_relation_size('benchmark_data') as size;")
        pg_table_size = cursor.fetchone()['size'] / (1024 * 1024)
        
        # Get File Sizes
        csv_size = os.path.getsize(CSV_FILE_LOCAL) / (1024 * 1024)
        parquet_size = os.path.getsize(PARQUET_FILE_LOCAL) / (1024 * 1024)
        
        print(f"{'Storage Format':<20} | {'Size (MB)':<10} | {'Ratio to Parquet':<15}")
        print("-" * 55)
        print(f"{'Postgres Table':<20} | {pg_table_size:<10.2f} | {pg_table_size/parquet_size:.2f}x")
        print(f"{'CSV File':<20} | {csv_size:<10.2f} | {csv_size/parquet_size:.2f}x")
        print(f"{'Parquet File':<20} | {parquet_size:<10.2f} | 1.00x")
        print(f"\nParquet is {100*(1 - parquet_size/pg_table_size):.1f}% smaller than Postgres Table")
        print(f"Parquet is {100*(1 - parquet_size/csv_size):.1f}% smaller than CSV")


        # 5. Scan/Read Speed Analysis
        print("\n=== Scan/Read Speed Analysis (Simulated OLAP Scan) ===")
        print("Measuring time to read all data...")
        
        # Postgres Scan
        # Force a sequential scan by disabling index scan if any (though we only have PK)
        cursor.execute("SET enable_indexscan = off;") 
        start_time = time.time()
        cursor.execute("SELECT count(*) FROM benchmark_data;") # Simple aggregation forces full scan
        cursor.fetchone()
        pg_scan_time = time.time() - start_time
        print(f"Postgres Table Scan: {pg_scan_time:.4f}s (Hot/Warm Cache)")
        
        # CSV Scan (Import to Temp)
        cursor.execute("DROP TABLE IF EXISTS temp_csv_import;")
        cursor.execute("CREATE TEMP TABLE temp_csv_import (LIKE benchmark_data INCLUDING ALL);")
        start_time = time.time()
        cursor.execute(f"COPY temp_csv_import FROM '{CSV_FILE_PG}' (FORMAT CSV);")
        csv_scan_time = time.time() - start_time
        print(f"CSV File Read:       {csv_scan_time:.4f}s")
        
        # Parquet Scan (Import to Temp)
        cursor.execute("DROP TABLE IF EXISTS temp_parquet_import;")
        cursor.execute("CREATE TEMP TABLE temp_parquet_import (LIKE benchmark_data INCLUDING ALL);")
        start_time = time.time()
        cursor.execute(f"COPY temp_parquet_import FROM '{PARQUET_FILE_PG}' (FORMAT 'parquet');")
        parquet_scan_time = time.time() - start_time
        print(f"Parquet File Read:   {parquet_scan_time:.4f}s")

        # 6. OLAP Query Simulation (On Database)
        print("\n=== OLAP Query Simulation (In-Database) ===")
        query = """
            SELECT date_col, count(*), avg(float_col) 
            FROM benchmark_data 
            GROUP BY date_col 
            ORDER BY date_col DESC 
            LIMIT 5;
        """
        print("Executing Group By Aggregation...")
        start_time = time.time()
        cursor.execute(query)
        rows = cursor.fetchall()
        olap_time = time.time() - start_time
        print(f"OLAP Query Time:     {olap_time:.4f}s")
        print("Sample Result:")
        for row in rows:
            print(f"  {row['date_col']}: Count={row['count']}, Avg={row['avg']:.2f}")


        # 7. Column Subset Import (Parquet Only)
        print("\n=== Column Subset Read (Parquet vs CSV) ===")
        subset_time = None
        try:
             cursor.execute("DROP TABLE IF EXISTS benchmark_subset;")
             cursor.execute("CREATE TABLE benchmark_subset (date_col DATE);")
             
             start_time = time.time()
             # Parquet: Only reads specific column
             cursor.execute(f"COPY benchmark_subset FROM '{PARQUET_FILE_PG}' (FORMAT 'parquet', match_by 'name');")
             subset_time = time.time() - start_time
             print(f"Parquet Col Read:    {subset_time:.4f}s (1 Column)")
             
             # CSV: Must read full rows (Postgres COPY doesn't support vertical slicing from CSV directly without file_fdw or program)
             # We use the full csv scan time as proxy for "Cost to access date_col from CSV"
             print(f"CSV Col Read:        {csv_scan_time:.4f}s (Must read full file)")
             
        except psycopg2.Error as e:
             print(f"Subset import failed/unsupported: {e}")
             conn.rollback()

        
        # 8. Summary
        print("\n=== Final Summary ===")
        print(f"{'Metric':<20} | {'Postgres':<10} | {'CSV':<10} | {'Parquet':<10} | {'Winner'}")
        print("-" * 70)
        print(f"{'Size (MB)':<20} | {pg_table_size:<10.2f} | {csv_size:<10.2f} | {parquet_size:<10.2f} | Parquet")
        print(f"{'Full Read (s)':<20} | {pg_scan_time:<10.4f} | {csv_scan_time:<10.4f} | {parquet_scan_time:<10.4f} | Postgres (RAM)")
        
        cursor.close()
        conn.close()
        print("\n✓ Benchmark completed successfully!")

    except psycopg2.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    main()
