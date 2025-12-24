import logging
import time
from concurrent.futures import as_completed, wait, CancelledError
from io import StringIO

from py_playground.taskqueue.mem_taskqueue import MemTaskQueue

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

def test_should_return():
    def fn() -> int:
        return 3

    queue = MemTaskQueue()
    queue.run()
    task = queue.submit(fn=fn)
    future = task.result()
    as_completed([future])

    queue.stop()
    assert future.result() == 3

#
def test_cancel():
    logging.info("Creating task...")
    logging.warning("this is warning")

    queue = MemTaskQueue()
    queue.run()

    tasks = [queue.submit(fn=lambda: time.sleep(0.5)) for _ in range(10)]
    # cancel all tasks
    queue.stop()

    cancelled_count = sum(1 for t in tasks if t.result().cancelled())
    assert cancelled_count > 0

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
