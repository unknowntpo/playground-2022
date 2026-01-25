# Datafusion Comet Playground

Minimal example: datafusion comet + spark + docker compose
Goal: Explore query plans to see Comet-accelerated execution

## Plan

### Files to Create
1. `docker-compose.yml` - Spark master/worker with Comet (image: `apache/datafusion-comet:0.12.0-spark3.5-scala2.12-java17`)
2. `scripts/generate_data.py` - Generate sample Parquet
3. `scripts/run_example.py` - PySpark script with `.explain()` to show Comet operators

### Key Spark Configs
```properties
spark.plugins=org.apache.spark.CometPlugin
spark.comet.enabled=true
spark.comet.exec.enabled=true
spark.comet.explainFallback.enabled=true
spark.memory.offHeap.enabled=true
spark.memory.offHeap.size=2g
```

### Query Plan Indicators
Look for `Comet*` prefixes in `df.explain()`:
- `CometScan` - Native Parquet scan
- `CometFilter` - Native filter
- `CometProject` - Native projection
- `CometHashAggregate` - Native aggregation

### Verification
```bash
docker compose up -d
docker compose exec spark-master spark-submit /app/scripts/run_example.py
# Check for CometScan, CometFilter in output
```

## References
- https://datafusion.apache.org/comet/
- https://hub.docker.com/r/apache/datafusion-comet
- https://github.com/apache/datafusion-comet