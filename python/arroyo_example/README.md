# Arroyo Stream Processing Example

Real-time Bitcoin price monitoring using [Arroyo](https://arroyo.dev), Kafka (AutoMQ), and Redis.

## Features

- **Real-time Ingestion**: Fetches live BTC-USD prices from Yahoo Finance via WebSocket.
- **Stream Processing**: Uses Arroyo (SQL) to compute:
  - 1-minute tumbling windows (10s size for demo).
  - 5-minute tumbling windows (30s size for demo).
  - Price spike alerts (>0.1% change).
- **Visualization**: Streamlit dashboard showing real-time feeds, charts, and alerts.
- **Infrastructure**: Fully dockerized stack including AutoMQ, MinIO, Redis.

## Architecture

1. **Source**: `price_producer.py` (yliveticker) -> Kafka `btc-price-ticks`.
2. **Processing**: Arroyo SQL Pipelines read from Kafka, aggregate, and write back to Kafka topics (`btc-window-1m`, `btc-alerts`, etc.).
3. **Sink**: `redis_writer.py` consumes output topics and updates Redis.
4. **UI**: Streamlit reads from Redis.

## Quick Start

See [QUICKSTART.md](QUICKSTART.md) for detailed instructions.

```bash
docker-compose up -d
```