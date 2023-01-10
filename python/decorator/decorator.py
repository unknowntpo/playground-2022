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

if __name__ == "__main__":
    dog_bark()
