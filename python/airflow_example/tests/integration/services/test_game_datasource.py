from datetime import datetime, timezone

from game_analytics.game_datasource import FakeKafkaDataSource
from game_analytics.game_event import EventType


async def test_kafka_datasource():
    num_events = 1000
    game_ids = [f"{i}" for i in range(10)]
    source = FakeKafkaDataSource(
        game_ids=game_ids,
        types=[EventType.Kill, EventType.Death],
        num_events=num_events,
        bootstrap_servers=["localhost:9092"],
        topic_name="test_game_event",
    )
    start = datetime(2025, 1, 6, 0, 0, 0, tzinfo=timezone.utc)
    end = datetime(2026, 1, 6, 0, 0, 0, tzinfo=timezone.utc)
    source(start=start, end=end)

    events = [e async for e in source]
    assert len(events) == num_events
