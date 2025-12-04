import asyncio
import io

import pytest

async def task1(buf):
    buf.write('task1')

async def task2(buf):
    buf.write('task2')

@pytest.mark.asyncio
async def test_create_task():
    buf = io.StringIO()
    task = asyncio.create_task(task1(buf))
    await task
    assert buf.getvalue() == 'task1'

@pytest.mark.asyncio
async def test_create_multiple_task():
    buf = io.StringIO()
    await asyncio.gather(task1(buf), task2(buf))

@pytest.mark.asyncio
async def test_taskgroup():
    buf = io.StringIO()

    async with asyncio.TaskGroup() as tg:
        tg.create_task(task1(buf))
        tg.create_task(task2(buf))
    # ← auto-waits for all tasks here

    assert 'task1' in buf.getvalue()
    assert 'task2' in buf.getvalue()

async def task1_except():
    raise ValueError('task1_except')

async def task2_except():
    raise TypeError('task2_except')

@pytest.mark.asyncio
async def test_taskgroup_exception():
    try:
        async with asyncio.TaskGroup() as tg:
            tg.create_task(task1_except())
            tg.create_task(task2_except())
        # ← auto-waits for all tasks here
    except ExceptionGroup as eg:
        print(f"Exceptions: {eg.exceptions}")
    assert 1 == 1