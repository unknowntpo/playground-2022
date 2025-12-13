"""Redis sink for publishing data to dashboard."""

import redis.asyncio as redis
import json
import os
from datetime import datetime
from src.models import WindowAggregation, Alert, PriceTick

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", "16379"))

# Redis keys
KEY_LATEST_PRICE = "btc:latest_price"
KEY_PRICE_HISTORY = "btc:price_history"
KEY_WINDOW_1M = "btc:window:1m"
KEY_WINDOW_5M = "btc:window:5m"
KEY_ALERTS = "btc:alerts"
KEY_ALERT_COUNT = "btc:alert_count"

# Lazy connection
_redis_client = None


async def get_redis():
    global _redis_client
    if _redis_client is None:
        _redis_client = await redis.from_url(
            f"redis://{REDIS_HOST}:{REDIS_PORT}", decode_responses=True
        )
    return _redis_client


async def publish_price_to_redis(tick: PriceTick):
    """Publish latest price and maintain history."""
    try:
        r = await get_redis()

        # tick.timestamp is now an ISO string from the producer
        ts_score = datetime.fromisoformat(tick.timestamp).timestamp()

        # Set latest price
        await r.set(
            KEY_LATEST_PRICE,
            json.dumps(
                {
                    "price": tick.price,
                    "timestamp": tick.timestamp,
                    "change_pct": tick.change_percent,
                    "updated_at": datetime.utcnow().isoformat(),
                }
            ),
        )

        # Add to sorted set (score = timestamp for ordering)
        await r.zadd(
            KEY_PRICE_HISTORY,
            {
                json.dumps(
                    {
                        "price": tick.price,
                        "timestamp": tick.timestamp,
                    }
                ): ts_score
            },
        )

        # Trim history to last 500 entries
        await r.zremrangebyrank(KEY_PRICE_HISTORY, 0, -501)
    except Exception as e:
        print(f"[ERROR] publish_price_to_redis: {e}")


async def publish_window_to_redis(agg: WindowAggregation, window_type: str):
    """Publish window aggregation to Redis."""
    try:
        r = await get_redis()
        key = KEY_WINDOW_1M if window_type == "1m" else KEY_WINDOW_5M

        await r.set(
            key,
            json.dumps(
                {
                    "symbol": agg.symbol,
                    "window_start": agg.window_start,
                    "window_end": agg.window_end,
                    "open_price": agg.open_price,
                    "close_price": agg.close_price,
                    "high_price": agg.high_price,
                    "low_price": agg.low_price,
                    "avg_price": agg.avg_price,
                    "price_change": agg.price_change,
                    "price_change_pct": agg.price_change_pct,
                    "tick_count": agg.tick_count,
                    "updated_at": datetime.utcnow().isoformat(),
                }
            ),
        )
    except Exception as e:
        print(f"[ERROR] publish_window_to_redis: {e}")


async def publish_alert_to_redis(alert: Alert):
    """Publish alert to Redis list (most recent first)."""
    try:
        r = await get_redis()

        # Push to list (LPUSH for most recent first)
        await r.lpush(
            KEY_ALERTS,
            json.dumps(
                {
                    "symbol": alert.symbol,
                    "alert_type": alert.alert_type,
                    "severity": alert.severity,
                    "message": alert.message,
                    "current_price": alert.current_price,
                    "reference_price": alert.reference_price,
                    "change_pct": alert.change_pct,
                    "triggered_at": alert.triggered_at,
                }
            ),
        )

        # Trim to last 50 alerts
        await r.ltrim(KEY_ALERTS, 0, 49)

        # Increment alert counter
        await r.incr(KEY_ALERT_COUNT)
    except Exception as e:
        print(f"[ERROR] publish_alert_to_redis: {e}")
