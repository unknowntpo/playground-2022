import asyncio
from unittest.result import failfast

from py_playground.taskqueue.mem_taskqueue import MemTaskQueue
from py_playground.taskqueue.taskqueue import Task

async def test_submit():
    queue = MemTaskQueue()
    x = 3
    await queue.run()

    def add_x():
        nonlocal x
        x += 1

    task: Task = await queue.submit(fn=add_x)
    await task
    await queue.stop()
    assert x == 4

async def test_submit_multiple():
    queue = MemTaskQueue()
    await queue.run()

    x = 3
    def add_x():
        nonlocal x
        x += 1

    content = ""
    def add_content():
        nonlocal content
        content += "hello"

    tasks = [
        queue.submit(fn=add_x),
        queue.submit(fn=add_content),
        queue.submit(fn=add_content)
    ]

    await asyncio.gather(*tasks)

    await queue.stop()
    assert x == 4
    assert content == "hellohello"
