from dataclasses import dataclass
from enum import Enum
from unicodedata import numeric


class EventType(Enum):
    Kill = "kill"
    Death = "Death"


@dataclass
class Event:
    player_id: str
    type: EventType


class GameDataGenerator:
    @classmethod
    def generate(
        cls, game_ids: list[str], types: list[EventType], num_events: int = 10
    ) -> list[Event]:
        """
        {"player_id": "p123", "event_type": "KILL", "game_id": "g001", "ts": "2025-12-30T10:00:00"},
        {"player_id": "p123", "event_type": "DEATH", "game_id": "g001", "ts": "2025-12-30T10:01:00"},
        """
        out = []
        while len(out) < num_events:
            for id in game_ids:
                for t in types:
                    out.append(Event(player_id=id, type=t))
        # FIXME: if num_events not enough, won't cover all cases
        return out[:num_events]
