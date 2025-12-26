import logging
import time

from py_playground.taskqueue.mem_taskqueue import MemTaskQueue
from py_playground.taskqueue.taskqueue import wait


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
        queue.submit(fn=add_content),
    ]

    futures = [task.result() for task in tasks]
    wait(futures)
    queue.stop()

    assert x == 4
    assert content == "hellohello"


def test_should_wait():
    queue = MemTaskQueue()

    def fn() -> int:
        time.sleep(0.1)
        return 3

    queue.run()
    t = queue.submit(fn=fn)
    wait([t])
    queue.stop()
    assert 3 == t.result().result()


def test_should_return():
    def fn() -> int:
        return 3

    queue = MemTaskQueue()
    queue.run()
    task = queue.submit(fn=fn)
    future = task.result()
    wait([future])

    queue.stop()
    assert future.result() == 3


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


def test_exception():
    e = Exception("intended exception")

    def fn_exception():
        raise e

    queue = MemTaskQueue()
    queue.run()

    task = queue.submit(fn=fn_exception)

    future = task.result()
    wait([future])
    assert e == future.exception()


#
#
#
#
#
