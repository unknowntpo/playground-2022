from dags.services.game_datasource import FakeDataSource, GameDataSource
from datetime import datetime

from dags.services.gamedata_generator import Event, EventType


def test_fake_datasource():
    source = FakeDataSource(game_ids=["1", "3"], types=[EventType.Kill, EventType.Death])
    start = datetime(2025, 1, 6, 0, 0, 0)
    end = datetime(2025, 1, 7, 0, 0, 0)
    source(start=start,end=end)
    events: list[Event] = list(source)
    out_of_range_events = list(filter(lambda e: not (start <= datetime.fromtimestamp(e.timestamp) < end), events))
    assert len(events) > 0
    assert len(out_of_range_events) == 0
