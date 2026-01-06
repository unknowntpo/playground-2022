from dataclasses import dataclass
from enum import Enum


class EventType(Enum):
    Kill = "kill"
    Death = "Death"


@dataclass
class Event:
    player_id: str
    type: EventType


class GameDataGenerator:
    def __init__(
        self, game_ids: list[str], types: list[EventType], num_events: int = 10
    ):
        min_events = len(game_ids) * len(types)
        if num_events < min_events:
            raise ValueError(
                f"num_events ({num_events}) must be >= {min_events} to cover all combinations"
            )
        self.game_ids = game_ids
        self.types = types
        self.num_events = num_events
        self.gen_cnt = 0

    def __iter__(self):
        self.gen_cnt = 0
        return self

    def __next__(self):
        if self.gen_cnt >= self.num_events:
            raise StopIteration

        total_combos = len(self.game_ids) * len(self.types)
        combo_idx = self.gen_cnt % total_combos
        game_idx = combo_idx // len(self.types)
        type_idx = combo_idx % len(self.types)

        event = Event(
            player_id=self.game_ids[game_idx],
            type=self.types[type_idx],
        )
        self.gen_cnt += 1
        return event
