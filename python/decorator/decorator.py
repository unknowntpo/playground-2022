def print_func_name(func):
    def wrap():
            print("Now use function '{}'".format(func.__name__))
            func()
    return wrap

def print_time(func):
    import time
    def wrap_2():
        print("Now the Unix time is {}".format(int(time.time())))
        func()
    return wrap_2

@print_func_name
@print_time
def dog_bark():
    print("Bark !!")


def mycache(func):
    cache: dict[int,int] = {}
    def wrapper(*args, **kargs):
        i = args[0]
        if i in cache:
            print(f"func({i}) in cache")
            return cache[i]
        print(f"func({i}) not in cache")
        cache[i] = func(*args, **kargs)
        return cache[i]
    return wrapper
        

@mycache
def fib(i: int)-> int:
    return i if i <= 1 else fib(i - 1) + fib(i-2)


if __name__ == "__main__":
    dog_bark()
    ans = fib(10)
    print(f"fib(10) = {ans}")
