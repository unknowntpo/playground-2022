package org.example;

public class Person {
    private final Calculator calculator;

    Person(Calculator calculator) {
        this.calculator = calculator;
    }

    public int add(int a, int b) {
        return this.calculator.add(a,b);
    }

    public int sub(int a, int b) {
       return this.calculator.sub(a,b);
    }
}
