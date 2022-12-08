from ctypes import CDLL

import pytest

@pytest.fixture
def libfact():
    yield CDLL("./fact.so")

def test_fact(libfact):
    assert libfact.fact(6) == 720
    assert libfact.fact(0) == 1
    assert libfact.fact(-42) == 1
