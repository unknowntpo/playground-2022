package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyThreadTest {

    @Test
    public void testMyThreadRun() throws InterruptedException {
        var l = new ArrayList<String>();
        var t = new MyThread(l);

        t.start();
        Thread.sleep(100);
        assertEquals(2, 1+1);
    }

    @Test
    public void testPrintHello() throws InterruptedException {
        var l = new ArrayList<String>();
        var t = new MyThread(l);

        t.start();
        Thread.sleep(100);
        assertEquals(l,Collections.nCopies(5, "Hello"));
    }
}
