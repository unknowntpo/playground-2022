from datetime import datetime

import polars as pl
from pathlib import Path
from typing import Protocol, Iterator, Self

from dags.services.game_event import EventType, Event
from dags.services.gamedata_generator import GameDataGenerator


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
            game_ids=["1", "2"],
            types=[EventType.Kill, EventType.Death],
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

def write_parquet_from_generator(gen: GameDataGenerator, output_file: Path):
    max_batch_size = 10
    batch = []

    for event in gen:
        print(f"got event: {event}")
        if len(batch) >= max_batch_size:
            df = pl.DataFrame(batch)
            if Path(output_file).exists():
                existing = pl.read_parquet(output_file)
                df = pl.concat([existing, df])
                df.write_parquet(output_file)
            else:
                df.write_parquet(output_file)
            batch = []
        batch.append(event.model_dump(mode="json"))

    if batch:
        df = pl.DataFrame(batch)
        if Path(output_file).exists():
            existing = pl.read_parquet(output_file)
            df = pl.concat([existing, df])
            df.write_parquet(output_file)
        else:
            df.write_parquet(output_file)


