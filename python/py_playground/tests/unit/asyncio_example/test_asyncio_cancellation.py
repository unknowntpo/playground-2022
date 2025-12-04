import asyncio
import io

import pytest


async def worker(name: str, buf: io.StringIO):
    """Worker that runs until cancelled"""
    try:
        count = 0
        while True:
            buf.write(f"{name}:{count} ")
            await asyncio.sleep(0.1)
            count += 1
    except asyncio.CancelledError:
        buf.write(f"{name}:cancelled ")
        raise  # must re-raise


async def worker_with_cleanup(name: str, buf: io.StringIO):
    """Worker with cleanup on cancellation"""
    try:
        while True:
            buf.write(f"{name}:working ")
            await asyncio.sleep(0.1)
    except asyncio.CancelledError:
        buf.write(f"{name}:cleanup ")
        raise
    finally:
        buf.write(f"{name}:finally ")


@pytest.mark.asyncio
async def test_manual_task_cancellation():
    """Cancel individual tasks manually"""
    buf = io.StringIO()

    task = asyncio.create_task(worker("w1", buf))
    await asyncio.sleep(0.25)

    task.cancel()

    with pytest.raises(asyncio.CancelledError):
        await task

    assert "w1:cancelled" in buf.getvalue()


@pytest.mark.asyncio
async def test_cancel_all_tasks_in_group():
    """Cancel all tasks in a group - similar to Go ctx.cancel()"""
    buf = io.StringIO()

    tasks = [
        asyncio.create_task(worker("w1", buf)),
        asyncio.create_task(worker("w2", buf)),
        asyncio.create_task(worker("w3", buf))
    ]

    await asyncio.sleep(0.25)

    # Cancel all tasks
    for task in tasks:
        task.cancel()

    # Wait for cancellation to complete
    results = await asyncio.gather(*tasks, return_exceptions=True)

    # All tasks should be cancelled
    assert all(isinstance(r, asyncio.CancelledError) for r in results)
    assert "w1:cancelled" in buf.getvalue()
    assert "w2:cancelled" in buf.getvalue()
    assert "w3:cancelled" in buf.getvalue()


@pytest.mark.asyncio
async def test_timeout_cancellation():
    """Timeout-based cancellation - similar to Go context.WithTimeout"""
    buf = io.StringIO()

    with pytest.raises(TimeoutError):
        async with asyncio.timeout(0.2):  # Python 3.11+
            await worker("w1", buf)

    # Worker should have been cancelled
    assert "w1:cancelled" in buf.getvalue()


@pytest.mark.asyncio
async def test_wait_for_with_timeout():
    """Alternative timeout using wait_for - Python 3.10 compatible"""
    buf = io.StringIO()

    with pytest.raises(asyncio.TimeoutError):
        await asyncio.wait_for(
            worker("w1", buf),
            timeout=0.2
        )

    assert "w1:cancelled" in buf.getvalue()


@pytest.mark.asyncio
async def test_event_based_cancellation():
    """Event-based cancellation - similar to Go's ctx.Done() channel"""
    buf = io.StringIO()
    cancel_event = asyncio.Event()

    async def worker_with_event(name: str):
        count = 0
        while not cancel_event.is_set():
            buf.write(f"{name}:{count} ")
            count += 1
            try:
                await asyncio.wait_for(asyncio.sleep(0.1), timeout=0.05)
            except asyncio.TimeoutError:
                continue  # check cancel_event again
        buf.write(f"{name}:stopped ")

    tasks = [
        asyncio.create_task(worker_with_event("w1")),
        asyncio.create_task(worker_with_event("w2"))
    ]

    await asyncio.sleep(0.25)
    cancel_event.set()  # Signal cancellation

    await asyncio.gather(*tasks)

    assert "w1:stopped" in buf.getvalue()
    assert "w2:stopped" in buf.getvalue()


@pytest.mark.asyncio
async def test_taskgroup_automatic_cancellation():
    """TaskGroup cancels all tasks on first exception"""
    buf = io.StringIO()

    async def failing_task():
        await asyncio.sleep(0.1)
        raise ValueError("Task failed!")

    with pytest.raises(ExceptionGroup) as exc_info:
        async with asyncio.TaskGroup() as tg:
            tg.create_task(failing_task())
            tg.create_task(worker("w1", buf))
            tg.create_task(worker("w2", buf))

    # Check that ValueError was raised
    assert any(isinstance(e, ValueError) for e in exc_info.value.exceptions)

    # Workers should have been cancelled
    assert "w1:cancelled" in buf.getvalue()
    assert "w2:cancelled" in buf.getvalue()


@pytest.mark.asyncio
async def test_cancellation_with_cleanup():
    """Ensure cleanup happens on cancellation"""
    buf = io.StringIO()

    task = asyncio.create_task(worker_with_cleanup("w1", buf))
    await asyncio.sleep(0.15)

    task.cancel()

    with pytest.raises(asyncio.CancelledError):
        await task

    output = buf.getvalue()
    assert "w1:cleanup" in output
    assert "w1:finally" in output


@pytest.mark.asyncio
async def test_shield_from_cancellation():
    """Shield task from cancellation - critical work must complete"""
    buf = io.StringIO()

    async def critical_work():
        buf.write("start ")
        await asyncio.sleep(0.2)
        buf.write("done ")

    async def do_work():
        # Shield critical work from cancellation
        await asyncio.shield(critical_work())

    task = asyncio.create_task(do_work())
    await asyncio.sleep(0.05)

    task.cancel()

    # Task is cancelled, but shielded work completes
    with pytest.raises(asyncio.CancelledError):
        await task

    # Wait a bit for shielded work to finish
    await asyncio.sleep(0.2)

    assert "start" in buf.getvalue()
    assert "done" in buf.getvalue()


@pytest.mark.asyncio
async def test_nested_cancellation():
    """Cancellation propagates through nested tasks"""
    buf = io.StringIO()

    async def outer():
        async def inner():
            try:
                await asyncio.sleep(10)
            except asyncio.CancelledError:
                buf.write("inner:cancelled ")
                raise

        try:
            await inner()
        except asyncio.CancelledError:
            buf.write("outer:cancelled ")
            raise

    task = asyncio.create_task(outer())
    await asyncio.sleep(0.05)

    task.cancel()

    with pytest.raises(asyncio.CancelledError):
        await task

    output = buf.getvalue()
    assert "inner:cancelled" in output
    assert "outer:cancelled" in output
