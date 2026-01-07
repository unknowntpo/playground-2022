import datetime
from typing import Protocol, Iterator, Iterable

from dags.services.gamedata_generator import GameDataGenerator, EventType, Event

class GameDataSource(Protocol):
    def set_range(self, *, start: datetime.datetime, end: datetime.datetime): ...
    def __iter__(self) -> Iterator[Event]: ...


class FakeDataSource:
    def __init__(self):
        self._generator = GameDataGenerator(
            game_ids=["1", "2"],
            types=[EventType.Kill, EventType.Death],
            num_events=10,
        )
        self.start: datetime.datetime | None = None
        self.end: datetime.datetime | None  = None

    def __iter__(self) -> Iterator[Event]:
        return iter(self._generator)

    def set_range(self, *, start: datetime.datetime, end: datetime.datetime):
        self.start = start
        self.end = end
