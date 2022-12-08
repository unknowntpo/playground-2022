from ctypes import CDLL

libfact = CDLL("./fact.so")

assert libfact.fact(6) == 720
assert libfact.fact(0) == 1
assert libfact.fact(-42) == 1
