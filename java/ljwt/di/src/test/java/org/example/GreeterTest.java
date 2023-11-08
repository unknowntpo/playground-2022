package org.example;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class GreeterTest {
    @Test
    public void testGreet() {
        Greeter greeter = new Greeter();
        assertEquals("Hello, Chris", greeter.greet());
    }
}