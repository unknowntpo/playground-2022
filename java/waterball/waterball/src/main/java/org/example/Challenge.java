package org.example;

public class Challenge {
    private int number;
    private String name;

    public Challenge(int number, String name) {
        setNumber(number);
        setName(name);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = ValidationUtils.shouldBePositive(number);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = ValidationUtils.lengthShouldBe(name, 1, 30);
    }
}
