import logging
import threading
from concurrent.futures import Future
from concurrent.futures.thread import ThreadPoolExecutor
from queue import Queue, Empty
from typing import Callable

from py_playground.taskqueue import TaskQueue
from py_playground.taskqueue.taskqueue import Task, Status

class MemTaskQueue(TaskQueue):
    def __init__(self):
        self._stop_event = threading.Event()
        self._queue = Queue()
        self.executor = ThreadPoolExecutor(max_workers=1)

    def run(self):
        self.executor.submit(self._worker)

    def submit(self, *, fn: Callable) -> Task:
        t = Task(fn)
        t.status = Status.INITIALIZED
        self._queue.put(t)
        return t

    def _worker(self):
        try:
            while not self._stop_event.is_set():
                # FIXME: can we dont uset timeout here ?
                try:
                    task: Task = self._queue.get(timeout=0.1)
                except Empty:
                    continue

                try:
                    task.status = Status.RUNNING
                    result = task.run()
                    task.status = Status.SUCCESS
                    task.set_result(result)
                except Exception as e:
                    task.status = Status.FAILED
                    task.set_exception(e)
            logging.info("_stop_event received")
            self._drain_and_cancel_queue()
        except Exception as e:
            logging.error(f"worker got exception: {e}")

    def _drain_and_cancel_queue(self):
        """Pop all queued tasks and mark them as cancelled."""
        while True:
            try:
                task = self._queue.get_nowait()
                task.status = Status.CANCELLED
                task._future.cancel()
            except Empty:
                break

    def stop(self):
        self._stop_event.set()
        self.executor.shutdown(True, cancel_futures=True)
