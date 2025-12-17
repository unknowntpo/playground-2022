from abc import ABC
from abc import abstractmethod
from enum import Enum
from typing import Callable, Protocol, TypeVar, Awaitable, Any


class Status(Enum):
    INITIALIZED = "INITIALIZED"
    QUEUED = "QUEUED"
    RUNNING = "RUNNING"
    SUCCESS = "SUCCESS"
    FAILED = "FAILED"

class Task(Protocol, Awaitable[Any]):
    @property
    def status(self) -> Status: ...
    @status.setter
    def status(self, value: Status): ...


class TaskQueue(ABC):
    """
    interface of TaskQueue
    """

    @abstractmethod
    async def submit(self, *, fn: Callable) -> Task:
        pass

    @abstractmethod
    async def run(self):
        pass

    @abstractmethod
    async def stop(self):
        pass