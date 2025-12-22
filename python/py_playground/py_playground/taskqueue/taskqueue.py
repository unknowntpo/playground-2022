from abc import ABC
from abc import abstractmethod
from concurrent.futures import Future
from enum import Enum
from typing import Callable, Protocol, TypeVar, Awaitable, Any


class Status(Enum):
    INITIALIZED = "INITIALIZED"
    QUEUED = "QUEUED"
    RUNNING = "RUNNING"
    SUCCESS = "SUCCESS"
    FAILED = "FAILED"

class Task(Protocol):
    @property
    def status(self) -> Status: ...
    @status.setter
    def status(self, value: Status): ...
    def run(self): ...
    def result(self) -> Future: ...


class TaskQueue(ABC):
    """
    interface of TaskQueue
    """

    @abstractmethod
    def submit(self, *, fn: Callable) -> Task:
        pass

    @abstractmethod
    def run(self):
        pass

    @abstractmethod
    def stop(self):
        pass