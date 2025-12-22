import asyncio
import logging
from concurrent.futures import as_completed
from io import StringIO

from py_playground.taskqueue.mem_taskqueue import MemTaskQueue
from py_playground.taskqueue.taskqueue import TaskQueue

def test_submit_multiple():
    queue = MemTaskQueue()
    queue.run()

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

    futures = [task.result() for task in tasks]
    as_completed(futures)
    queue.stop()

    assert x == 4
    assert content == "hellohello"
#
# async def test_cancel():
#     buf = StringIO()
#
#     async def fn():
#         try:
#             await asyncio.sleep(2)
#             buf.write("hello")
#         except asyncio.CancelledError:
#             logging.warning("Task 1 cancelled")
#
#     logging.warning("Creating task...")
#
#     task1 = asyncio.create_task(fn())
#     task1.cancel()
#
#     try:
#         await task1  # Must await to run task
#     except asyncio.CancelledError:
#         logging.warning("Caught CancelledError")
#
#     print(f"\n=== Buffer: '{buf.getvalue()}' ===")
#     assert buf.getvalue() == ""
#
# async def test_exception():
#     def fn_exception():
#         raise Exception("intended exception")
#
#     queue: TaskQueue = MemTaskQueue()
#     await queue.run()
#
#     task = await queue.submit(fn_exception)
#
#     assert task._future.resul
#
#
#
#
#
#
