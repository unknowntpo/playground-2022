# This is a sample Python script.
import asyncio

# Press ⌃F5 to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press F9 to toggle the breakpoint.

async def co1():
    print("co1 preparing task")
    await asyncio.sleep(1)
    print("co1 done task")
    return 1

def task_done_callback(task: asyncio.Task[int]):
    i = task.result()
    print(f"task_done_callback is executed: {i}")

async def coroutine_example():
    res: int = asyncio.run(co1())
    print(res)

    # play with task
    t1 = asyncio.create_task(co1())
    t1.add_done_callback(task_done_callback)
    await t1

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')
    asyncio.run(coroutine_example())

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
