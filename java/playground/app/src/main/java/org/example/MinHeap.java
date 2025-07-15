package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MinHeap<E extends Comparable<E>> {
    PriorityQueue<E> queue;
    public MinHeap(List<E> list) {
        queue = new PriorityQueue<E>();
        list.forEach(e -> queue.offer(e));
    }

    public List<E> min(int k) {
        var res = new ArrayList<E>();
        while (k > 0) {
            res.add(queue.poll());
            k--;
        }
        return res;
    }
}
