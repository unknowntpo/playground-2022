from typing import Callable

task_queue = []
micro_task_queue = []

class Promise:
    def __init__(self, cb: Callable):
        self.cb = cb
        micro_task_queue.append(cb)

def run(f: Callable):
    # Local queues for this run() invocation
    task_queue.append(f)
    # Event loop: process tasks, then microtasks
    while task_queue:
        task = task_queue.pop(0)
        task()

    while micro_task_queue:
        task = micro_task_queue.pop(0)
        task()



