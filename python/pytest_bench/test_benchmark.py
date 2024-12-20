import time

def something(arg1, arg2, arg3, foo=None):
    time.sleep(0.03)

def something_fast(arg1, arg2, arg3, foo=None):
    time.sleep(0.01)

def my_special_setup():
    pass

def test_something(benchmark):
    benchmark.pedantic(something, setup=my_special_setup, args=(1, 2, 3), kwargs={'foo': 'bar'}, iterations=1, rounds=100)
def test_something_fast(benchmark):
    benchmark.pedantic(something_fast, setup=my_special_setup, args=(1, 2, 3), kwargs={'foo': 'bar'}, iterations=1, rounds=100)

