#!/usr/bin/env python3
"""Generate sample Parquet data for Comet testing."""

from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, IntegerType, StringType, DoubleType
import random

def main():
    spark = SparkSession.builder \
        .appName("GenerateSampleData") \
        .master("local[*]") \
        .getOrCreate()

    # Generate sample data
    num_rows = 100000

    data = [
        (
            i,
            f"user_{i % 1000}",
            random.choice(["electronics", "books", "clothing", "food", "toys"]),
            round(random.uniform(10.0, 1000.0), 2),
            random.randint(1, 100)
        )
        for i in range(num_rows)
    ]

    schema = StructType([
        StructField("id", IntegerType(), False),
        StructField("user_id", StringType(), True),
        StructField("category", StringType(), True),
        StructField("price", DoubleType(), True),
        StructField("quantity", IntegerType(), True)
    ])

    df = spark.createDataFrame(data, schema)

    # Write as Parquet
    output_path = "/app/data/sample.parquet"
    df.write.mode("overwrite").parquet(output_path)

    print(f"Generated {num_rows} rows at {output_path}")
    print(f"Schema: {df.schema}")
    df.show(5)

    spark.stop()

if __name__ == "__main__":
    main()
