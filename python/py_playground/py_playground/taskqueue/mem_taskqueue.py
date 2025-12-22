import asyncio
import logging
import os
from concurrent.futures import Future, CancelledError
from concurrent.futures.thread import ThreadPoolExecutor
from queue import Queue
from typing import Callable, Coroutine, Any, Protocol

from py_playground.taskqueue import TaskQueue
from py_playground.taskqueue.taskqueue import Task, Status
from tests.unit.asyncio_example.test_asyncio_cancellation import worker

class MemTask:
    def __init__(self, fn: Callable):
        self._fn = fn
        self._status = Status.INITIALIZED
        self._future = Future()

    @property
    def status(self) -> Status:
        return self._status

    @status.setter
    def status(self, value: Status):
        self._status = value

    def run(self):
        return self._fn()

    def result(self) -> Future:
        return self._future

class MemTaskQueue(TaskQueue):
    def __init__(self):
        self._queue = Queue()
        self.executor = ThreadPoolExecutor(max_workers=1)

    def run(self):
        self.executor.submit(self._worker)

    def submit(self, *, fn: Callable) -> Task:
        t = MemTask(fn)
        t.status = Status.INITIALIZED
        self._queue.put(t)
        return t

    def _worker(self):
        try:
            while True:
                # FIXME: can we dont uset timeout here ?
                task: MemTask = self._queue.get(timeout=0.1)
                try:
                    task.status = Status.RUNNING
                    result = task.run()
                    task.status = Status.SUCCESS
                    task.result().set_result(result)
                except Exception as e:
                    task.status = Status.FAILED
                    task.result().set_exception(e)
        except CancelledError:
            logging.debug("worker got cancelled")
            pass

    def stop(self):
        self.executor.shutdown(True, cancel_futures=True)
