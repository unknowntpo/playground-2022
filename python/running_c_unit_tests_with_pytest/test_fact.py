from ctypes import CDLL

import pytest


@pytest.fixture
def libfact():
    yield CDLL("./fact.so")


@pytest.mark.parametrize("n,e", [(6, 720), (0, 1), (-42, 1)])
def test_fact(libfact, n, e):
    assert libfact.fact(n) == e
