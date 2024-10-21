from abc import ABC, abstractmethod



class Car(ABC):
    @abstractmethod
    def run(self):
        pass

class Toyota(Car):
    def run(self):
        print("run by toyota")

def main():
    toyota = Toyota() 
    toyota.run()


if __name__ == "__main__":
    main()
