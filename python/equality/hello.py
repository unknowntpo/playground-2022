class Hello: 
    def __init__(self, a: int, b: int) -> None:
        self.a = a
        self.b = b

def main():
    objA = Hello(3, 2)
    objB = Hello(3, 2)

    print(objA is objB) # False
    print(objA == objB) # False



if __name__ == "__main__":
    main()
