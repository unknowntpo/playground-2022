package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadsDemoTest {

    @Test
    public void testMultiThreadAdd() throws InterruptedException {
        ThreadsDemo threadsDemo = new ThreadsDemo();
        assertDoesNotThrow(() -> {
            assertEquals(3 * 20, threadsDemo.multiThreadAdd(3, 20));
        });
    }

}
