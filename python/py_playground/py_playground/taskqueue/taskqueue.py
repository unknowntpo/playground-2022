from abc import ABC
from abc import abstractmethod
from concurrent.futures import Future
from concurrent import futures
from enum import Enum
from typing import Callable, Protocol, overload, runtime_checkable


class Status(Enum):
    INITIALIZED = "INITIALIZED"
    QUEUED = "QUEUED"
    RUNNING = "RUNNING"
    SUCCESS = "SUCCESS"
    FAILED = "FAILED"
    CANCELLED = "CANCELLED"


class Task:
    def status(self) -> Status: ...
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


@overload
def wait(tasks: list[Task]) -> None: ...
@overload
def wait(tasks: list[Future]) -> None: ...
def wait(tasks: list[Future] | list[Task]) -> None:
    """
    wait for task to be done.
    """
    if tasks and isinstance(tasks[0], Task):
        futures.wait([t.result() for t in tasks])
    else:
        futures.wait(tasks)
