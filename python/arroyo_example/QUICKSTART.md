# Quick Start Guide

## 1. Install Dependencies (Optional - for local development)

If you want to run scripts locally without Docker:
```bash
uv sync
```

## 2. Start Infrastructure

```bash
docker-compose up -d
sleep 30  # Wait for services to be healthy and pipelines to be initialized
docker-compose ps
```

Expected: All services running (server1..3, redis, minio, console, arroyo, arroyo-init, redis-writer, price-producer, dashboard).

`arroyo-init` might exit with code 0 after submitting pipelines.

## 3. Verify Components

1. **Dashboard**: Open [http://localhost:8501](http://localhost:8501)
   - Should show real-time BTC prices, aggregations, and alerts.

2. **Arroyo UI**: Open [http://localhost:5115](http://localhost:5115)
   - Check "Pipelines" tab. You should see `btc-1m-window`, `btc-5m-window`, `btc-alerts` running.
   - Click on a pipeline to see metrics (events/sec).

3. **Redpanda Console** (Kafka UI): Open [http://localhost:8080](http://localhost:8080)
   - Topics: `btc-price-ticks`, `btc-window-1m`, `btc-window-5m`, `btc-alerts`

## Architecture

```text
[yliveticker] 
    ↓ (price-producer)
[Kafka: btc-price-ticks]
    ↓ (Arroyo Pipeline: 1m, 5m, Alerts)
[Kafka: btc-window-1m, btc-alerts, ...]
    ↓ (redis-writer)
[Redis]
    ↓
[Streamlit Dashboard]
```

## Troubleshooting

### "Pipeline failed"
Check Arroyo logs:
```bash
docker-compose logs arroyo
```

### "No data in Dashboard"
Check if `price-producer` is working:
```bash
docker-compose logs price-producer
```
Check if `redis-writer` is working:
```bash
docker-compose logs redis-writer
```

### "Arroyo init failed"
If pipelines are not submitted, you can try running init again:
```bash
docker-compose restart arroyo-init
```
Or run locally:
```bash
uv run python init_arroyo.py
```