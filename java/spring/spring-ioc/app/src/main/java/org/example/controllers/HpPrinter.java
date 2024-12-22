package org.example.controllers;

import org.springframework.stereotype.Component;

@Component
public class HpPrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.printf("print from %s: %s\n", HpPrinter.class.toString(), message);
    }
}
