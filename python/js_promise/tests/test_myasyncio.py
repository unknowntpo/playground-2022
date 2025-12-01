import myasyncio.myasyncio as aio
import io



def test_run():
    buf = io.StringIO()
    def f1():
        buf.write('hello in f1\n')

    def f():
        f1()
        p = aio.Promise(lambda: buf.write('hello in promise\n'))
        buf.writelines('end of f\n')

    aio.run(f)
    expect_sequence = f"""hello in f1
end of f
hello in promise
"""

    assert buf.getvalue() == expect_sequence


def test_run_isolation():
    """Test that multiple run() calls don't share queues"""
    buf1 = io.StringIO()
    buf2 = io.StringIO()

    def f1():
        buf1.write('run1-task\n')
        aio.Promise(lambda: buf1.write('run1-promise\n'))
        buf1.write('run1-end\n')

    def f2():
        buf2.write('run2-task\n')
        aio.Promise(lambda: buf2.write('run2-promise\n'))
        buf2.write('run2-end\n')

    # Run first event loop
    aio.run(f1)

    # Run second event loop - should not affect first
    aio.run(f2)

    # Verify each run had isolated execution
    expected1 = """run1-task
run1-end
run1-promise
"""
    expected2 = """run2-task
run2-end
run2-promise
"""

    assert buf1.getvalue() == expected1
    assert buf2.getvalue() == expected2

def test_nested_promises():
    """Test that multiple run() calls don't share queues"""
    buf = io.StringIO()

    def f1():
        buf.write('f1-start\n')
        aio.Promise(f2)
        buf.write('f1-end\n')

    def f2():
        buf.write('f2-start\n')
        aio.Promise(lambda: buf.write('nested-promise\n'))
        buf.write('f2-end\n')


    # Run first event loop
    aio.run(f1)

    # Verify each run had isolated execution
    expected = """f1-start
f1-end
f2-start
f2-end
nested-promise
"""
    assert buf.getvalue() == expected
