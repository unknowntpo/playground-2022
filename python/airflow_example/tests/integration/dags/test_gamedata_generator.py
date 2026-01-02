from dags.services.gamedata_generator import EventType, GameDataGenerator


def test_hello():
    assert 1 == 1


def test_generate_event():
    """
    {"player_id": "p123", "event_type": "KILL", "game_id": "g001", "ts": "2025-12-30T10:00:00"},
    {"player_id": "p123", "event_type": "DEATH", "game_id": "g001", "ts": "2025-12-30T10:01:00"},
    """

    gen = GameDataGenerator()
    events = gen.generate(
        game_ids=["1", "3"], types=[EventType.Kill, EventType.Death], num_events=10
    )
    assert len(events) == 10
    assert (
        len(list(filter(lambda e: e.type in [EventType.Kill, EventType.Death], events)))
        > 0
    )
