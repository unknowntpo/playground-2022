package org.example;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;


import org.junit.jupiter.api.Test;

import java.io.IOException;

class GreeterTest {
    @Test
    public void testGreet() {
        BufferWriter buffer = new BufferWriter();
        Greeter greeter = new Greeter(buffer);
        String name = "Chris";
        String want = "Hello, " + name;
        try {
            greeter.greet(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(want, buffer.toString());
    }
}