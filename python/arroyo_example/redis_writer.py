import os
import asyncio
import json
import logging
from aiokafka import AIOKafkaConsumer
from src.redis_sink import publish_price_to_redis, publish_window_to_redis, publish_alert_to_redis
from types import SimpleNamespace

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

async def process_price_tick(msg):
    try:
        data = json.loads(msg.value)
        # logger.info(f"Received tick data: {data}")
        # Convert to object expected by redis_sink
        tick = SimpleNamespace(**data)
        await publish_price_to_redis(tick)
        logger.info(f"Processed tick for {tick.symbol} at {tick.timestamp}")
    except Exception as e:
        logger.error(f"Error processing tick: {e}")

async def process_window(msg, window_type):
    try:
        data = json.loads(msg.value)
        # Convert to object
        agg = SimpleNamespace(**data)
        await publish_window_to_redis(agg, window_type)
        logger.info(f"Processed {window_type} window for {agg.symbol}")
    except Exception as e:
        logger.error(f"Error processing window {window_type}: {e}")

async def process_alert(msg):
    try:
        data = json.loads(msg.value)
        alert = SimpleNamespace(**data)
        await publish_alert_to_redis(alert)
        logger.info(f"Processed alert for {alert.symbol}")
    except Exception as e:
        logger.error(f"Error processing alert: {e}")

async def consume():
    kafka_bootstrap = os.getenv('KAFKA_BOOTSTRAP', 'localhost:9092')
    logger.info(f"Connecting to Kafka at {kafka_bootstrap}...")
    
    # Wait for Kafka to be ready (simple retry loop logic handled by aiokafka internally usually, but we can wrap it)
    consumer = AIOKafkaConsumer(
        'btc-price-ticks',
        'btc-window-1m',
        'btc-window-5m',
        'btc-alerts',
        bootstrap_servers=kafka_bootstrap,
        group_id='redis-writer-group-v2',
        auto_offset_reset='earliest' # Start from beginning if no offset
    )
    
    while True:
        try:
            await consumer.start()
            logger.info("Connected to Kafka. consuming...")
            break
        except Exception as e:
            logger.warning(f"Failed to connect to Kafka, retrying in 5s: {e}")
            await asyncio.sleep(5)

    try:
        async for msg in consumer:
            topic = msg.topic
            if topic == 'btc-price-ticks':
                await process_price_tick(msg)
            elif topic == 'btc-window-1m':
                await process_window(msg, '1m')
            elif topic == 'btc-window-5m':
                await process_window(msg, '5m')
            elif topic == 'btc-alerts':
                await process_alert(msg)
    finally:
        await consumer.stop()

if __name__ == '__main__':
    asyncio.run(consume())