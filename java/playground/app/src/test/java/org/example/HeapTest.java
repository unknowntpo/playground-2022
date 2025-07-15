package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeapTest {
    @Test
    public void testMinHeap() {
        var heap = new Heap<>(Heap.HeapType.MIN, Set.of(1, 3, 5, 7).stream().toList());
        int k = 3;

        var ans = heap.min(k);

        assertEquals(ans, List.of(1, 3, 5));
    }

    @Test
    public void testMaxHeap() {
        var heap = new Heap<>(Heap.HeapType.MAX, Set.of(1, 3, 5, 7).stream().toList());
        int k = 3;

        var ans = heap.min(k);

        assertEquals(ans, List.of(7, 5, 3));
    }
}
