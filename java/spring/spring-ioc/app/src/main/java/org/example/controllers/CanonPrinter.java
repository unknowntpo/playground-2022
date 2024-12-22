package org.example.controllers;

import org.springframework.stereotype.Component;

@Component
public class CanonPrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.printf("print from %s, %s\n", CanonPrinter.class.toString(), message);
    }
}
