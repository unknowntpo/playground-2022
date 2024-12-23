package org.example.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class HpPrinter implements Printer {
    private int count;

    @PostConstruct
    void initialize() {
        count = 5;
    }

    @Override
    public void print(String message) {
        System.out.printf("print from %s: %s, count: %d\n", HpPrinter.class.toString(), message, count);
    }
}
