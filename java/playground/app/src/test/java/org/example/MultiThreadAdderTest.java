package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiThreadAdderTest {

    @Test
    public void testAddMultiThread() {
        var adder = new MultiThreadAdder();
        int cnt = 100;
        int total = adder.add(cnt);
        assertEquals(cnt, total);
    }
}
