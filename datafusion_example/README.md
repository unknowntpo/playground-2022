# DataFusion Comet + Spark Example

Minimal Docker Compose setup to run Spark with DataFusion Comet acceleration and explore query plans.

## Platform Compatibility

| Platform | Comet Support | Notes |
|----------|---------------|-------|
| x86_64 (Intel/AMD) | ✅ Full | Native Comet acceleration works |
| ARM64 (Apple Silicon) | ⚠️ Limited | Comet native library is x86 only |

## Quick Start

### Standard Spark (works on all platforms)

```bash
# Start Spark container
docker compose up -d

# Generate sample data
docker compose exec spark spark-submit /app/scripts/generate_data.py

# Run example (without Comet on ARM64)
docker compose exec spark spark-submit /app/scripts/run_example.py
```

### With Comet (x86_64 only)

For x86 systems or CI/cloud environments:

```bash
# Edit docker-compose.yml:
# - Comment out the 'spark' service
# - Uncomment the 'spark-comet' service

# Start with Comet image
docker compose up -d

# Generate sample data
docker compose exec spark-comet bash -c \
  "PYTHONPATH=/opt/spark/python python3 /app/scripts/generate_data.py"

# Run example with Comet enabled
docker compose exec spark-comet bash -c \
  "ENABLE_COMET=true PYTHONPATH=/opt/spark/python python3 /app/scripts/run_example.py"
```

## Web UI

- Spark Application: http://localhost:4040 (when job is running)

## Comet Operators

When running with Comet enabled, `df.explain()` shows these operators:

| Operator | Description |
|----------|-------------|
| `CometScan` | Native Parquet reading |
| `CometFilter` | Native filtering |
| `CometProject` | Native projection |
| `CometHashAggregate` | Native aggregation |
| `CometSort` | Native sorting |
| `CometBroadcastHashJoin` | Native broadcast join |

### Example Output (with Comet)

```
== Physical Plan ==
*(1) ColumnarToRow
+- CometFilter [price], (isnotnull(price) AND (price > 500.0))
   +- CometScan parquet [id,user_id,category,price,quantity]
```

### Example Output (without Comet)

```
== Physical Plan ==
*(1) Filter (isnotnull(price#2) AND (price#2 > 500.0))
+- *(1) ColumnarToRow
   +- FileScan parquet [id#0,user_id#1,category#2,price#3,quantity#4]
```

## Cleanup

```bash
docker compose down -v
rm -rf data/sample.parquet
```

## References

- [DataFusion Comet Docs](https://datafusion.apache.org/comet/)
- [Comet Docker Images](https://hub.docker.com/r/apache/datafusion-comet)
- [Comet GitHub](https://github.com/apache/datafusion-comet)
