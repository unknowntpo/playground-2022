from typing import Callable
from contextvars import ContextVar

# Each run() gets its own queues via context vars
_task_queue: ContextVar[list] = ContextVar('task_queue')
_micro_task_queue: ContextVar[list] = ContextVar('micro_task_queue')

class Promise:
    def __init__(self, cb: Callable):
        self.cb = cb
        _micro_task_queue.get().append(cb)

def run(f: Callable):
    # Local queues for this run() invocation
    task_queue = [f]
    micro_task_queue = []

    # Set context for this run
    _task_queue.set(task_queue)
    _micro_task_queue.set(micro_task_queue)

    # Event loop: process tasks, then microtasks
    while task_queue:
        task = task_queue.pop(0)
        task()

    while micro_task_queue:
        task = micro_task_queue.pop(0)
        task()



