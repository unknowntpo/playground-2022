import asyncio
import logging
from datetime import datetime

import aiokafka
import pyarrow as pa
import pyarrow.parquet as pq
from pathlib import Path
from typing import Protocol, Iterator, Self, Any, Callable, AsyncIterator

from aiokafka import AIOKafkaProducer, AIOKafkaConsumer
from aiokafka.admin import AIOKafkaAdminClient, NewTopic
from more_itertools.more import consumer
from structlog.processors import CallsiteParameterAdder

from game_analytics.game_event import EventType, Event
from game_analytics.gamedata_generator import GameDataGenerator


class GameDataSource(Protocol):
    async def __call__(self, *, start: datetime, end: datetime): ...
    def __iter__(self) -> Iterator[Event]: ...


class FakeDataSource:
    def __init__(self, game_ids: list[str], types: list[EventType], num_events=10):
        self.game_ids = game_ids
        self.types = types
        self.start: datetime | None = None
        self.end: datetime | None = None
        self.num_events = num_events

    def _init_generator(self):
        self._generator = GameDataGenerator(
            game_ids=self.game_ids,
            types=self.types,
            num_events=self.num_events,
            start=self.start,
            end=self.end,
        )

    def __iter__(self) -> Iterator[Event]:
        self._init_generator()
        return iter(self._generator)

    async def __call__(self, *, start: datetime, end: datetime) -> Self:
        self.start = start
        self.end = end
        return self


class FakeKafkaDataSource:
    def __init__(self, game_ids: list[str], types: list[EventType], num_events=10, bootstrap_servers=["localhost:9092"],
                 topic_name="test_game_event"):
        self.game_ids = game_ids
        self.types = types
        self.start: datetime | None = None
        self.end: datetime | None = None
        self.num_events = num_events
        self.num_consumed_events = 0
        self.bootstrap_servers = bootstrap_servers
        self.topic_name = topic_name

    def __call__(self, *, start: datetime, end: datetime) -> Self:
        self.start = start
        self.end = end
        return self

    async def __aiter__(self) -> AsyncIterator[Event]:
        self._init_generator()
        await self._init_kafka()
        await self._inject_data()

        async for msg in self.consumer:
            yield msg.value
            self.num_consumed_events += 1
            if self.num_consumed_events >= self.num_events:
                break

    def _init_generator(self):
        self._generator = GameDataGenerator(
            game_ids=self.game_ids,
            types=self.types,
            num_events=self.num_events,
            start=self.start,
            end=self.end,
        )

    async def _init_kafka(self):
        await self._reset_topic()
        value_serializer: Callable[[Event], dict] = lambda v: v.model_dump_json().encode("utf-8")
        self.producer = AIOKafkaProducer(bootstrap_servers=self.bootstrap_servers, value_serializer=value_serializer)
        await self.producer.start()
        value_deserializer: Callable[[dict], Event] = lambda str: Event.model_validate_json(str)
        self.consumer = AIOKafkaConsumer(self.topic_name, bootstrap_servers=self.bootstrap_servers, value_deserializer=value_deserializer)
        await self.consumer.start()

    async def _inject_data(self):
        for event in self._generator:
            await self.producer.send(self.topic_name, event)

    async def _reset_topic(self):
        admin = AIOKafkaAdminClient(bootstrap_servers=self.bootstrap_servers)
        await admin.start()
        try:
            await admin.delete_topics([self.topic_name])
            await asyncio.sleep(0.5)
        except Exception as e:
            logging.error(f"topic not exist: {e}")
        await admin.create_topics([NewTopic(self.topic_name, 3, 2)])
        await asyncio.sleep(0.5)
        await admin.close()


# Define schema based on Event fields
SCHEMA = pa.schema([
    ("player_id", pa.string()),
    ("type", pa.string()),
    ("timestamp", pa.float64()),
])

def write_parquet_from_generator(gen: GameDataGenerator, output_file: Path):
    max_batch_size = 10
    batch = []
    writer = pq.ParquetWriter(output_file, SCHEMA)

    for event in gen:
        print(f"got event: {event}")
        if len(batch) >= max_batch_size:
            flush_to_parquet(batch, writer)
            batch = []
        batch.append(event.model_dump(mode="json"))

    if batch:
        flush_to_parquet(batch, writer)

def flush_to_parquet(batch: list[Any], writer: pq.ParquetWriter):
    table = pa.Table.from_pylist(batch)
    writer.write_table(table)




