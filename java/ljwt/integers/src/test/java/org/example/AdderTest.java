package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdderTest {
    @Test
    @DisplayName("1 + 1 = 2")
    public void testAdd() {
        Adder adder = new Adder();
        assertEquals(3, adder.add(1, 2) );
    }
}