package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Heap<E extends Comparable<E>> {
    public enum HeapType {
        MIN,
        MAX
    }

    PriorityQueue<E> queue;

    public Heap(HeapType type, List<E> list) {
        switch (type) {
            case MIN -> {
                queue = new PriorityQueue<E>();
                list.forEach(e -> queue.offer(e));
                break;
            }
            case MAX -> {
                queue = new PriorityQueue<E>(Comparator.reverseOrder());
                list.forEach(e -> queue.offer(e));
                break;
            }
        }
    }

    public List<E> min(int k) {
        var res = new ArrayList<E>();
        while (k > 0) {
            res.add(queue.poll());
            k--;
        }
        return res;
    }

    public List<E> max(int k) {
        var res = new ArrayList<E>();
        while (k > 0) {
            res.add(queue.poll());
            k--;
        }
        return res;
    }
}


