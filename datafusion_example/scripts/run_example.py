#!/usr/bin/env python3
"""
DataFusion Comet Example: Explore Query Plans with Native Acceleration.

This script demonstrates how to use Comet with Spark and inspect
query plans to see which operations are accelerated.

Usage:
  - ARM64 (Apple Silicon): Runs without Comet (shows standard Spark plans)
  - x86_64: Runs with Comet enabled (shows Comet operators)
"""

import os
import platform
from pyspark.sql import SparkSession
from pyspark.sql import functions as F

def is_comet_available():
    """Check if Comet JAR is available."""
    import glob
    patterns = [
        "/opt/spark/jars/comet-spark-spark*.jar",
        "/opt/spark/comet-jars/comet-spark-spark*.jar"
    ]
    for pattern in patterns:
        matches = [f for f in glob.glob(pattern) if not f.endswith(('-sources.jar', '-test-sources.jar'))]
        if matches:
            return True
    return False

def create_spark_session(enable_comet=False):
    """Create Spark session, optionally with Comet enabled."""
    builder = SparkSession.builder \
        .appName("CometExample") \
        .master("local[*]") \
        .config("spark.driver.memory", "1g")

    if enable_comet:
        print("Enabling DataFusion Comet acceleration...")
        builder = builder \
            .config("spark.plugins", "org.apache.spark.CometPlugin") \
            .config("spark.comet.enabled", "true") \
            .config("spark.comet.exec.enabled", "true") \
            .config("spark.comet.scan.enabled", "true") \
            .config("spark.comet.explainFallback.enabled", "true") \
            .config("spark.memory.offHeap.enabled", "true") \
            .config("spark.memory.offHeap.size", "1g")
    else:
        print("Running without Comet (standard Spark execution)")

    return builder.getOrCreate()

def main():
    print("=" * 60)
    print("DataFusion Comet Query Plan Exploration")
    print("=" * 60)
    print(f"Platform: {platform.machine()}")

    # Check if we should enable Comet
    use_comet = os.environ.get("ENABLE_COMET", "false").lower() == "true"
    if use_comet and not is_comet_available():
        print("Warning: ENABLE_COMET=true but Comet JAR not found. Running without Comet.")
        use_comet = False

    spark = create_spark_session(enable_comet=use_comet)

    # Read Parquet data
    df = spark.read.parquet("/app/data/sample.parquet")

    print("\n" + "=" * 60)
    print("1. Simple Scan" + (" - Look for CometScan" if use_comet else ""))
    print("=" * 60)
    df.explain(True)

    print("\n" + "=" * 60)
    print("2. Filter Query" + (" - Look for CometFilter" if use_comet else ""))
    print("=" * 60)
    filtered = df.filter(F.col("price") > 500)
    filtered.explain(True)
    print(f"\nFiltered count: {filtered.count()}")

    print("\n" + "=" * 60)
    print("3. Projection" + (" - Look for CometProject" if use_comet else ""))
    print("=" * 60)
    projected = df.select("id", "category", "price")
    projected.explain(True)

    print("\n" + "=" * 60)
    print("4. Aggregation" + (" - Look for CometHashAggregate" if use_comet else ""))
    print("=" * 60)
    agg = df.groupBy("category").agg(
        F.sum("price").alias("total_sales"),
        F.avg("price").alias("avg_price"),
        F.count("*").alias("num_orders")
    )
    agg.explain(True)
    print("\nAggregation results:")
    agg.show()

    print("\n" + "=" * 60)
    print("5. Complex Query" + (" - Multiple Comet Operators" if use_comet else ""))
    print("=" * 60)
    complex_query = df \
        .filter(F.col("quantity") > 50) \
        .select("category", "price", "quantity") \
        .groupBy("category") \
        .agg(
            F.sum(F.col("price") * F.col("quantity")).alias("revenue"),
            F.max("price").alias("max_price")
        ) \
        .orderBy(F.desc("revenue"))

    complex_query.explain(True)
    print("\nComplex query results:")
    complex_query.show()

    print("\n" + "=" * 60)
    print("6. Join Query" + (" - Look for CometBroadcastHashJoin" if use_comet else ""))
    print("=" * 60)
    categories = spark.createDataFrame([
        ("electronics", "Tech"),
        ("books", "Media"),
        ("clothing", "Fashion"),
        ("food", "Grocery"),
        ("toys", "Kids")
    ], ["category", "department"])

    joined = df.join(categories, "category")
    joined.explain(True)
    print("\nJoined sample:")
    joined.show(5)

    print("\n" + "=" * 60)
    if use_comet:
        print("Summary: Look for 'Comet*' operators in plans above")
        print("=" * 60)
        print("""
Comet operators to look for:
- CometScan: Native Parquet reading
- CometFilter: Native filtering
- CometProject: Native projection
- CometHashAggregate: Native aggregation
- CometSort: Native sorting
- CometBroadcastHashJoin: Native broadcast join

If you see 'ColumnarToRow' conversions, those indicate
transitions between Comet native and Spark JVM execution.
""")
    else:
        print("Note: Running without Comet (standard Spark execution)")
        print("=" * 60)
        print("""
To see Comet acceleration, run on x86_64 with:
  export ENABLE_COMET=true

Or use the Comet Docker image on x86 systems.
See README.md for details.
""")

    spark.stop()

if __name__ == "__main__":
    main()
