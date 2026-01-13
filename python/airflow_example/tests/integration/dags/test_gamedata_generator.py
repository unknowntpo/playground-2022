from datetime import datetime

from dags.services.gamedata_generator import GameDataGenerator
from dags.services.game_event import EventType

def test_generate_event():
    """
    {"player_id": "p123", "event_type": "KILL", "game_id": "g001", "ts": "2025-12-30T10:00:00"},
    {"player_id": "p123", "event_type": "DEATH", "game_id": "g001", "ts": "2025-12-30T10:01:00"},
    """

    start = datetime(2025,1,6,0,0,0)
    end = datetime(2025,1,7,0,0,0)

    num_events = 100

    gen = GameDataGenerator(game_ids=["1", "3"], types=[EventType.Kill, EventType.Death], start=start, end=end, num_events=num_events)
    events = list(gen)

    assert len(events) == num_events
    assert (
        len(list(filter(lambda e: e.type in [EventType.Kill, EventType.Death], events)))
        > 0
    )
    assert len(list(filter(lambda e: e.player_id in ["1", "3"], events))) > 0
