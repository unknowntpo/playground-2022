#!/usr/bin/env python3
"""
Producer: Yahoo Finance WebSocket -> Kafka
Publishes real-time BTC-USD prices to AutoMQ cluster.
Uses yfinance protobuf definitions and websockets library.
Simulates high-frequency updates between real ticks for demo purposes.
"""

import json
import os
import signal
import sys
import asyncio
import websockets
import base64
import random
from datetime import datetime
from aiokafka import AIOKafkaProducer
from yfinance.pricing_pb2 import PricingData
from google.protobuf.json_format import MessageToDict

KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP", "localhost:9092")
TOPIC = "btc-price-ticks"
SYMBOLS = ["BTC-USD"]
YAHOO_WS_URL = "wss://streamer.finance.yahoo.com/"

producer = None
loop = None
latest_price_data = {}  # Store the latest real data to base synthetic ticks on


def decode_message(base64_message):
    try:
        decoded_bytes = base64.b64decode(base64_message)
        pricing_data = PricingData()
        pricing_data.ParseFromString(decoded_bytes)
        return MessageToDict(pricing_data, preserving_proto_field_name=True)
    except Exception as e:
        print(f"Error decoding protobuf: {e}", flush=True)
        return None


async def init_producer():
    """Initialize Kafka producer with retry logic."""
    global producer
    retry_count = 0
    max_retries = 20

    while retry_count < max_retries:
        try:
            producer = AIOKafkaProducer(
                bootstrap_servers=KAFKA_BOOTSTRAP,
                value_serializer=lambda v: json.dumps(v).encode("utf-8"),
            )
            await producer.start()
            print(
                f"[{datetime.now()}] Kafka producer connected to {KAFKA_BOOTSTRAP}",
                flush=True,
            )
            return
        except Exception as e:
            retry_count += 1
            print(
                f"[{datetime.now()}] Kafka connection failed (attempt {retry_count}/{max_retries}): {e}",
                flush=True,
            )
            await asyncio.sleep(5)

    print(
        f"[{datetime.now()}] Failed to connect to Kafka after {max_retries} attempts. Exiting.",
        flush=True,
    )
    sys.exit(1)


async def close_producer():
    """Close Kafka producer."""
    if producer:
        await producer.stop()
        print(f"[{datetime.now()}] Kafka producer closed", flush=True)


async def send_tick_to_kafka(tick, is_synthetic=False):
    if producer:
        await producer.send(TOPIC, value=tick)
        tag = "[SYNTHETIC]" if is_synthetic else "[REAL]"
        print(
            f"[{datetime.now().strftime('%H:%M:%S.%f')[:-3]}] {tag} "
            f"{tick['symbol']}: ${tick['price']:.2f} ({tick['change_percent']:+.2f}%)",
            flush=True,
        )


async def produce_tick(data):
    """Transform and send data to Kafka."""
    if not data:
        return

    try:
        symbol = data.get("id")
        if symbol not in SYMBOLS:
            return

        price = float(data.get("price", 0))
        
        # Store for synthetic generation
        latest_price_data[symbol] = {
            "price": price,
            "day_high": data.get("dayHigh"),
            "day_low": data.get("dayLow"),
            "volume": data.get("dayVolume"),
            "base_change": float(data.get("change", 0)),
            "base_change_percent": float(data.get("changePercent", 0)),
        }

        tick = {
            "symbol": symbol,
            "price": price,
            'timestamp': datetime.fromtimestamp(int(data.get('time', 0)) / 1000).isoformat(),
            "change": float(data.get("change", 0)),
            "change_percent": float(data.get("changePercent", 0)),
            "day_high": data.get("dayHigh"),
            "day_low": data.get("dayLow"),
            "volume": data.get("dayVolume"),
        }

        await send_tick_to_kafka(tick, is_synthetic=False)

    except Exception as e:
        print(f"[ERROR] producing tick: {e}", flush=True)

async def synthetic_tick_generator():
    """Generate high-frequency synthetic ticks based on last real price."""
    while True:
        try:
            if not latest_price_data:
                await asyncio.sleep(1)
                continue

            for symbol in SYMBOLS:
                if symbol not in latest_price_data:
                    continue
                
                base_data = latest_price_data[symbol]
                current_price = base_data["price"]
                
                # Random walk: -0.05% to +0.05% change
                change_pct = (random.random() - 0.5) * 0.001 
                new_price = current_price * (1 + change_pct)
                
                # Update base slightly so it drifts
                base_data["price"] = new_price
                
                tick = {
                    "symbol": symbol,
                    "price": new_price,
                    'timestamp': datetime.now().isoformat(), # Use current time
                    "change": base_data["base_change"] + (new_price - current_price), # approx
                    "change_percent": (new_price - base_data["price"]) / base_data["price"] * 100, # approx
                    "day_high": base_data["day_high"],
                    "day_low": base_data["day_low"],
                    "volume": base_data["volume"],
                }
                
                await send_tick_to_kafka(tick, is_synthetic=True)

            await asyncio.sleep(0.5) # 2 ticks per second

        except Exception as e:
            print(f"Error in synthetic generator: {e}", flush=True)
            await asyncio.sleep(1)


async def run_websocket():
    """Maintain WebSocket connection."""
    while True:
        try:
            print(f"[{datetime.now()}] Connecting to {YAHOO_WS_URL}...", flush=True)
            async with websockets.connect(
                YAHOO_WS_URL,
                additional_headers={"Origin": "https://finance.yahoo.com"}
            ) as websocket:
                print(f"[{datetime.now()}] Connected to Yahoo Finance WS.", flush=True)

                # Subscribe
                msg = json.dumps({"subscribe": SYMBOLS})
                await websocket.send(msg)
                print(f"[{datetime.now()}] Subscribed to {SYMBOLS}", flush=True)

                async for message in websocket:
                    data = decode_message(message)
                    await produce_tick(data)

        except Exception as e:
            print(f"[{datetime.now()}] WebSocket error: {e}. Reconnecting in 5s...", flush=True)
            await asyncio.sleep(5)


def signal_handler(signum, frame):
    """Handle shutdown signals."""
    print(f"\n[{datetime.now()}] Shutting down...", flush=True)
    sys.exit(0)


async def main():
    """Main entry point."""
    global loop
    loop = asyncio.get_event_loop()

    await init_producer()

    # Run WebSocket and Synthetic Generator concurrently
    websocket_task = asyncio.create_task(run_websocket())
    synthetic_task = asyncio.create_task(synthetic_tick_generator())

    try:
        await asyncio.gather(websocket_task, synthetic_task)
    except asyncio.CancelledError:
        pass
    finally:
        await close_producer()


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        pass