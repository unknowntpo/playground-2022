import asyncio
import logging
from io import StringIO

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

async def test_cancel():
    buf = StringIO()

    async def fn():
        try:
            await asyncio.sleep(2)
            buf.write("hello")
        except asyncio.CancelledError:
            logging.warning("Task 1 cancelled")

    logging.warning("Creating task...")

    task1 = asyncio.create_task(fn())
    task1.cancel()

    try:
        await task1  # Must await to run task
    except asyncio.CancelledError:
        logging.warning("Caught CancelledError")

    print(f"\n=== Buffer: '{buf.getvalue()}' ===")
    assert buf.getvalue() == ""


