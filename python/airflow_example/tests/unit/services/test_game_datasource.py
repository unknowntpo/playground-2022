from dags.services.game_datasource import FakeDataSource, GameDataSource
from datetime import datetime, timezone

from dags.services.gamedata_generator import Event


def test_fake_datasource():
    src: GameDataSource = FakeDataSource()
    start = datetime(2025, 1, 6, 0, 0, 0)
    end = datetime(2025, 1, 7, 0, 0, 0)
    events: list[Event] = list(src)
    src.set_range(
        start=start,
        end=end,
    )
    out_of_range_events = list(filter(lambda e: not (start <= datetime.fromtimestamp(e.timestamp) <= end), events))
    assert len(events) > 0
    assert len(out_of_range_events) == 0
