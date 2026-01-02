# Taipei Weather Data Crawler

## Problem
- Need historical Taipei weather data for analytics
- Crawl from public API, transform, store as Parquet
- Use Polars for efficient DataFrame ops

## Concepts
- **TaskFlow API**: `@task` decorator (Airflow 3.x)
- **Polars**: Fast DataFrame library (Rust-based)
- **Parquet**: Columnar storage format
- **LocalStack**: Local S3 emulation

## Data Source
**Open-Meteo Historical Weather API** (free, no key)
- URL: `https://archive-api.open-meteo.com/v1/archive`
- Taipei coords: lat=25.0330, lon=121.5654

## Input (API Response)
```json
{
  "hourly": {
    "time": ["2024-01-01T00:00", ...],
    "temperature_2m": [15.2, ...],
    "relative_humidity_2m": [80, ...],
    "precipitation": [0.0, ...],
    "wind_speed_10m": [12.5, ...]
  }
}
```

## Output (Parquet Schema)
| Column           | Type    |
|------------------|---------|
| time             | String  |
| temperature_c    | Float64 |
| humidity_pct     | Float64 |
| precipitation_mm | Float64 |
| wind_speed_kmh   | Float64 |

## DAG
```python
from airflow.sdk import dag, task
from datetime import datetime

@dag(
    dag_id="taipei_weather_crawler",
    start_date=datetime(2024, 1, 1),
    schedule="@daily",
    catchup=False,
    tags=["weather", "taipei", "etl"],
)
def taipei_weather_crawler():

    @task
    def fetch_weather(date_str: str) -> dict:
        import httpx
        resp = httpx.get(
            "https://archive-api.open-meteo.com/v1/archive",
            params={
                "latitude": 25.0330,
                "longitude": 121.5654,
                "start_date": date_str,
                "end_date": date_str,
                "hourly": "temperature_2m,relative_humidity_2m,precipitation,wind_speed_10m"
            }
        )
        return resp.json()

    @task
    def transform_to_parquet(data: dict) -> bytes:
        import polars as pl
        import io
        hourly = data["hourly"]
        df = pl.DataFrame({
            "time": hourly["time"],
            "temperature_c": hourly["temperature_2m"],
            "humidity_pct": hourly["relative_humidity_2m"],
            "precipitation_mm": hourly["precipitation"],
            "wind_speed_kmh": hourly["wind_speed_10m"],
        })
        buf = io.BytesIO()
        df.write_parquet(buf)
        return buf.getvalue()

    @task
    def upload_to_s3(parquet_bytes: bytes, date_str: str):
        import boto3
        s3 = boto3.client(
            "s3",
            endpoint_url="http://localstack:4566",
            aws_access_key_id="test",
            aws_secret_access_key="test",
        )
        key = f"weather/taipei/{date_str}.parquet"
        s3.put_object(Bucket="weather-data", Key=key, Body=parquet_bytes)
        return f"s3://weather-data/{key}"

    date_str = "{{ ds }}"
    raw = fetch_weather(date_str)
    parquet = transform_to_parquet(raw)
    upload_to_s3(parquet, date_str)

taipei_weather_crawler()
```

## Flow
```
Open-Meteo API → fetch_weather → transform_to_parquet → upload_to_s3
                                      (Polars)           (LocalStack)
```

## LocalStack (docker-compose)
```yaml
localstack:
  image: localstack/localstack:latest
  ports:
    - "4566:4566"
  environment:
    - SERVICES=s3
    - DEFAULT_REGION=ap-northeast-1
  volumes:
    - localstack-data:/var/lib/localstack
```

## Dependencies
- polars
- httpx
- boto3
