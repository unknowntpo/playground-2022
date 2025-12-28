import time
from abc import ABC
from abc import abstractmethod
from concurrent.futures import Future
from concurrent import futures
from enum import Enum
from typing import Callable, Protocol, overload, runtime_checkable, TypeVar, Generic


class Status(Enum):
    INITIALIZED = "INITIALIZED"
    QUEUED = "QUEUED"
    RUNNING = "RUNNING"
    SUCCESS = "SUCCESS"
    FAILED = "FAILED"
    CANCELLED = "CANCELLED"

T = TypeVar("T")

class Task(Generic[T]):
    def __init__(self, fn: Callable):
        self.fn = fn
        self.status = Status.INITIALIZED
        self._result: T | None = None
    def run(self):
        self._result = self.fn()

    def is_done(self) -> bool:
        return self.status in (Status.SUCCESS, Status.FAILED, Status.CANCELLED)

    def result(self) -> T:
        """
        Block until status
        :return:
        """
        while not self.is_done():
            time.sleep(0.01)
        return self._result

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
