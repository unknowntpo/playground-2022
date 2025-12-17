import asyncio
import logging
from typing import Callable, Coroutine, Any, Protocol

from py_playground.taskqueue import TaskQueue
from py_playground.taskqueue.taskqueue import Task, Status

class MemTask:
    def __init__(self, fn: Callable):
        self._fn = fn
        self._status = Status.INITIALIZED
        self._future = asyncio.Future()

    def __await__(self):
        return self._future.__await__()

    @property
    def status(self) -> Status:
        return self._status

    @status.setter
    def status(self, value: Status):
        self._status = value

    async def run(self):
        res = self._fn()
        self._future.set_result(res)


class MemTaskQueue(TaskQueue):
    def __init__(self):
        self._queue = asyncio.Queue[MemTask]()
        self._worker_task = None

    async def run(self):
        self._worker_task = asyncio.create_task(self._worker())

    async def submit(self, *, fn: Callable) -> Task:
        t = MemTask(fn)
        t.status = Status.INITIALIZED
        await self._queue.put(t)
        return t

    async def _worker(self):
        try:
            while True:
                task: MemTask = await self._queue.get()
                try:
                    task.status = Status.RUNNING
                    result = await task.run()
                    task.status = Status.SUCCESS
                except Exception as e:
                    task.set_exception(e)
                    task.status = Status.FAILED
        except asyncio.CancelledError:
            logging.debug("worker got cancelled")
            pass

    async def stop(self):
        self._worker_task.cancel()
        await self._worker_task
