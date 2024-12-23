package org.example.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HpPrinter implements Printer {
    private int count;

    @Value("${my.printerLimit}")
    private int printerLimit;

    @PostConstruct
    void initialize() {
        count = 5;
    }

    @Override
    public void print(String message) {
        System.out.printf("print from %s: %s, count: %d, printerLimit: %d\n", HpPrinter.class.toString(), message, count, printerLimit);
    }
}
