package org.example;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GreeterTest {
    @Test
    public void testGreet() {
        Greeter greeter = new Greeter();
        String name = "Chris";
        assertEquals("Hello, " + name, greeter.greet(name));
    }
}