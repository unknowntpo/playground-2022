package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueueTest {
    @Test
    void testAddToQueue() {
        Queue<String> queue = new Queue<String>();
        List<String> strs = new ArrayList<>(Arrays.asList("hello","world"));
        for (String str: strs) {
            queue.add(str);
        }
        assertEquals(queue.list(), strs);
    }
}
