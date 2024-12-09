from abc import ABC, abstractmethod


class Car(ABC):
    def drive(self):
        print(f"I'm driving, I'm {self.__class__.__name__}, my brand is {self.brand()}")

    @abstractmethod
    def brand(self) -> str:
        pass


class Benz(Car):
    def brand(self):
        # Benz
        return self.__class__.__name__


if __name__ == "__main__":
    c = Benz()
    c.brand()
    c.drive()
