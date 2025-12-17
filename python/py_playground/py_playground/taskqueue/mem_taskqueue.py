import asyncio
from asyncio import Queue
from typing import Callable, Coroutine

from py_playground.taskqueue import TaskQueue
from py_playground.taskqueue.taskqueue import Task, Status


class MemTask(Task):
    def __init__(self, fn: Callable):
        self._fn = fn
        self._status = Status.INITIALIZED
        self._future = asyncio.Future()

    def __await__(self):
        return self._future.__await__()

    @property
    def status(self) -> Status :
        return self._status

    @status.setter
    def status(self, value: Status):
        self._status = value

class MemTaskQueue(TaskQueue):
    def __init__(self):
        self._queue = asyncio.Queue[Task]()
        self._worker_task = None

    async def run(self):
        self._worker_task = asyncio.create_task(self._worker())

    async def submit(self, *, fn: Callable) -> Task:
        t = MemTask(fn)
        await self._queue.put(t)
        return t

    async def _worker(self):
        while True:
            task: Task = await self._queue.get()
            try:
                task.status = Status.RUNNING
                result = await task
                task.status = Status.SUCCESS
                task.set_result(result)
            except Exception as e:
                task.set_exception(e)
                task.status = Status.FAILED
            finally:
                self._queue.task_done()

    async def stop(self):
        self._worker_task.cancel()
        await self._worker_task