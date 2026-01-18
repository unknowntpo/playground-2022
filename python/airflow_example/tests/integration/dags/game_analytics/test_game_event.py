from datetime import datetime, tzinfo, timezone

from game_analytics.game_event import Event, EventType



def test_serialize():
    e = Event(
        player_id="1",
        type=EventType.Kill,
        timestamp=datetime.now(tz=timezone.utc).timestamp()
    )

    # Serialize to dict
    serialized = e.model_dump(mode="json")
    assert isinstance(serialized, dict)
    assert serialized["type"] == "kill"

    # Deserialize from dict
    deserialized = Event.model_validate(serialized)
    assert deserialized == e

def test_serialize_json_string():
    e = Event(
        player_id="1",
        type=EventType.Kill,
        timestamp=1234567890.0
    )

    # Serialize to JSON string
    json_str = e.model_dump_json()
    assert json_str == '{"player_id":"1","type":"kill","timestamp":1234567890.0}'

    # Deserialize from JSON string
    deserialized = Event.model_validate_json(json_str)
    assert deserialized == e