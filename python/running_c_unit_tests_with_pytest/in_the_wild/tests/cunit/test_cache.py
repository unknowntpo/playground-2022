from ctypes import CDLL

import pytest
from tests.cunit import SRC, compile


import platform
if platform.system() == "Linux":
    C = CDLL("libc.so.6")
else:
    # For MacOS
    C = CDLL("/usr/lib/libc.dylib")

@pytest.fixture
def cache():
    source = SRC / "cache.c"
    compile(source)
    yield CDLL(str(source.with_suffix(".so")))

NULL = 0

def test_queue_item(cache):
    value = C.malloc(16)
    queue_item = cache.queue_item_new(value, 42)
    assert queue_item

    cache.queue_item__destroy(queue_item, C.free)


@pytest.mark.parametrize("qsize", [0, 10, 100, 1000])
def test_queue(cache, qsize):
    q = cache.queue_new(qsize, C.free)

    assert cache.queue__is_empty(q)
    assert qsize == 0 or not cache.queue__is_full(q)

    assert cache.queue__dequeue(q) is NULL

    values = [C.malloc(16) for _ in range(qsize)]
    assert all(values)

    for k, v in enumerate(values):
        assert cache.queue__enqueue(q, v, k)

    assert qsize == 0 or not cache.queue__is_empty(q)
    assert cache.queue__is_full(q)
    assert cache.queue__enqueue(q, 42, 42) is NULL

    assert values == [cache.queue__dequeue(q) for _ in range(qsize)]
