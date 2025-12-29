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
        self._future = Future[T]()
    def run(self):
        return self.fn()

    def is_done(self) -> bool:
        return self.status in (Status.SUCCESS, Status.FAILED, Status.CANCELLED)

    def set_result(self, res: T):
        self._future.set_result(res)

    def set_exception(self, e: BaseException | None):
        self._future.set_exception(e)

    def result(self, timeout: float | None = None):
        return self._future.result(timeout=timeout)

    def cancelled(self):
        return self.status == Status.CANCELLED

    def exception(self, timeout: float | None = None) -> BaseException | None:
        return self._future.exception()


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


def wait(tasks: list[Task]) -> None:
    """
    wait for tasks to be done.
    """
    futures.wait([t._future for t in tasks])
