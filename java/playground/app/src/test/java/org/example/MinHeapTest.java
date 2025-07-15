package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinHeapTest {
    @Test
    public void testMinHeap() {
        // push 6 eles
        var heap = new MinHeap<>(Set.of(1, 3, 5, 7).stream().toList());
        int k = 3;

        var ans = heap.min(k);

        assertEquals(ans, List.of(1, 3, 5));
    }

}
