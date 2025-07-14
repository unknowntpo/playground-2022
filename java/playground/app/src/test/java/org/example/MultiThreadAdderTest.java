package org.example;

import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class MultiThreadAdderTest {

    @RepeatedTest(10)
    public void testAddMultiThread() {
        var adder = new MultiThreadAdder();
        int cnt = 10000;
        int total = adder.add(cnt);
        assertEquals(cnt, total);
    }
}
