from typing import Iterable, Protocol, Iterator

from dags.services.gamedata_generator import GameDataGenerator, EventType


class GameDataSource(Protocol, Iterator): ...

class FakeDataSource:
    def __init__(self):
        self._generator = GameDataGenerator(self, game_ids=["1", "2"],types=[EventType.Kill, EventType.Death] , num_events= 10)
    def __iter__(self):
        self._generator.__iter__()
    def __next__(self):
        self._generator.__next__()

