from datetime import datetime
from typing import Protocol, Iterator, Self

from dags.services.gamedata_generator import GameDataGenerator, EventType, Event


class GameDataSource(Protocol):
    def __call__(self, *, start: datetime, end: datetime): ...
    def __iter__(self) -> Iterator[Event]: ...


class FakeDataSource:
    def __init__(self, game_ids: list[str], types: list[EventType]):
        self.game_ids = game_ids
        self.types = types
        self.start: datetime | None = None
        self.end: datetime | None = None

    def _init_generator(self):
        self._generator = GameDataGenerator(
            game_ids=["1", "2"],
            types=[EventType.Kill, EventType.Death],
            num_events=10,
            start=self.start,
            end=self.end,
        )

    def __iter__(self) -> Iterator[Event]:
        self._init_generator()
        return iter(self._generator)

    def __call__(self, *, start: datetime, end: datetime) -> Self:
        self.start = start
        self.end = end
        return self
