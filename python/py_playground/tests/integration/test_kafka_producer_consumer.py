import asyncio
import datetime
import json
import random

from aiokafka import AIOKafkaProducer, AIOKafkaConsumer
from pydantic import BaseModel

BOOTSTRAP_SERVERS="localhost:9092"
TOPIC="user_event"

producer_done = asyncio.Event()

class Event(BaseModel):
    user_id: int
    action: str
    timestamp: float

async def produce():
    producer = AIOKafkaProducer(
        bootstrap_servers=BOOTSTRAP_SERVERS,
        # FIXME: understand h ow json works in py
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )
    await producer.start()

    n = 100
    try:
        for _ in range(n):
            ev = Event(user_id=random.randint(0, 100), action="run", timestamp=datetime.datetime.now().timestamp())
            await producer.send_and_wait(TOPIC, ev.model_dump())
            print(f"produced {ev}")
        print(f"done producing {n} records")
    except Exception as e:
        print(e)
    finally:
        await producer.stop()
        producer_done.set()

async def consume():
    consumer = AIOKafkaConsumer(
        TOPIC,
        bootstrap_servers=BOOTSTRAP_SERVERS,
        value_deserializer=lambda v: json.loads(v.decode("utf-8")),
        auto_offset_reset="earliest"
    )

    await consumer.start()

    try:
        while not producer_done.is_set():
            msg = await asyncio.wait_for(consumer.getone(), timeout=1.0)
            ev = Event(**msg.value)
            print(f"consumed {ev}")
    finally:
        await consumer.stop()


async def test_kafka():
    await asyncio.gather(produce(), consume())
    assert 1 == 1


