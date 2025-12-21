

from concurrent.futures import ThreadPoolExecutor, as_completed
from io import StringIO

def test_futures_wait():
    buf = StringIO()

    def f1():
        buf.write("hello f1\n")
        return "result from f1"
    def f2():
        buf.write("hello f2\n")
        return "result from f2"

    executor = ThreadPoolExecutor()
    tasks = [f1, f2]
    futures = [executor.submit(f) for f in tasks]

    for future in as_completed(futures):
        result = future.result()
        assert result.startswith("result from")
    assert "hello f1\nhello f2\n" == buf.getvalue()