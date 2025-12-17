from unittest.result import failfast

from py_playground.taskqueue.mem_taskqueue import MemTaskQueue
from py_playground.taskqueue.taskqueue import Task

async def test_submit() -> None:
    queue = MemTaskQueue()
    x: int = 3
    await queue.run()

    try:
        task: Task = await queue.submit(fn=lambda: x + 1)
        await task
        await queue.stop()
    finally:
        assert 4 == x