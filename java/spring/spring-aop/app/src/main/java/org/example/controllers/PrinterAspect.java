package org.example.controllers;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PrinterAspect {

    @Before("execution(* org.example.controllers.HpPrinter.*(..))")
    public void before() {
        System.out.printf(" at %s, Before printer printing...", PrinterAspect.class.getCanonicalName());
    }
}
