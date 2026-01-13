import random
from pathlib import Path

import polars as pl
from dags.services.game_datasource import (
    FakeDataSource,
    write_parquet_from_generator,
)
from dags.services.game_event import EventType, Event
from datetime import datetime


def test_fake_datasource():
    source = FakeDataSource(
        game_ids=["1", "3"], types=[EventType.Kill, EventType.Death]
    )
    start = datetime(2025, 1, 6, 0, 0, 0)
    end = datetime(2025, 1, 7, 0, 0, 0)
    source(start=start, end=end)
    events: list[Event] = list(source)
    out_of_range_events = list(
        filter(
            lambda e: not (start <= datetime.fromtimestamp(e.timestamp) < end), events
        )
    )
    assert len(events) > 0
    assert len(out_of_range_events) == 0


def test_write_parquet_from_generator():
    num_events = 100
    source = FakeDataSource(
        game_ids=["1", "3"],
        types=[EventType.Kill, EventType.Death],
        num_events=num_events,
    )
    start = datetime(2025, 1, 6, 0, 0, 0)
    end = datetime(2025, 1, 8, 0, 0, 0)
    source(start=start, end=end)
    file_path = Path(f"/tmp/test{random.randint(1, 100)}.parquet")
    print(file_path)
    write_parquet_from_generator(source, file_path)

    df = pl.read_parquet(file_path)
    print(df)
    assert len(df) == num_events
