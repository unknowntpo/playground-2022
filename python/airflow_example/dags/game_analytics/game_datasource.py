from datetime import datetime

import pyarrow as pa
import pyarrow.parquet as pq
from pathlib import Path
from typing import Protocol, Iterator, Self, Any

from game_analytics.game_event import EventType, Event
from game_analytics.gamedata_generator import GameDataGenerator


class GameDataSource(Protocol):
    def __call__(self, *, start: datetime, end: datetime): ...
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

    def __call__(self, *, start: datetime, end: datetime) -> Self:
        self.start = start
        self.end = end
        return self

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




