package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyThreadTest {

    @Test
    public void testMyThreadRun() throws InterruptedException {
        Queue<String> q = new ConcurrentLinkedDeque<>();
        var t = new MyThread(q, 1);

        t.start();
        Thread.sleep(100);
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testPrintHello() throws InterruptedException {
        Queue<String> queue = new ConcurrentLinkedDeque<>();
        var t = new MyThread(queue, 5);

        t.start();
        Thread.sleep(100);
        assertEquals(Collections.nCopies(5, "Hello"), new ArrayList<>(queue));
    }
}
