from datetime import timezone, datetime
from pathlib import Path

from game_analytics.game_datasource import (
    FakeKafkaDataSource,
    GameDataSource,
    FakeDataSource,
)
from game_analytics.game_event import EventType
from tests.integration.utils.utils import run_airflow_cli

PROJECT_ROOT = Path(
    __file__
).parent.parent.parent.parent  # tests/integration/dags -> project root
DAGS_FOLDER = PROJECT_ROOT / "dags"


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
    await source(start=start, end=end)

    events = [e async for e in source]
    assert len(events) == num_events

async def test_consume_events():
    from game_analytics.game_analytics import consume_events
    events = await consume_events()
    assert len(events) == 10



async def test_aggregate_to_parquet():
    num_events = 1000
    game_ids = [f"{i}" for i in range(10)]
    source: GameDataSource = FakeKafkaDataSource(
        game_ids=game_ids,
        types=[EventType.Kill, EventType.Death],
        num_events=num_events,
        bootstrap_servers=["localhost:9092"],
        topic_name="test_game_event",
    )
    start = datetime(2025, 1, 6, 0, 0, 0, tzinfo=timezone.utc)
    end = datetime(2026, 1, 6, 0, 0, 0, tzinfo=timezone.utc)
    source(start=start, end=end)

    result = run_airflow_cli([
        "airflow", "dags", "test", "game_data_injection", "2025-01-15"
    ])
    assert result.returncode == 0, f"DAG test failed: {result.stderr}"







