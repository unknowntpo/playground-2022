from ctypes import CDLL

import pytest
from tests.cunit import SRC, compile

C = CDLL("libc.so.6")


@pytest.fixture
def cache():
    source = SRC / "cache.c"
    compile(source)
    yield CDLL(str(source.with_suffix(".so")))


def test_cache(cache):
    lru_cache = cache.lru_cache_new(10, C.free)
    assert lru_cache
    cache.lru_cache__destroy(lru_cache)
