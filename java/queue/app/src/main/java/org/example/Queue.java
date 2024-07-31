package org.example;

import java.util.ArrayList;
import java.util.List;

public class Queue<T> {
    ArrayList<T> queue;

    public Queue() {
        this.queue = new ArrayList<>();
    }

    public void add(T element) {
        this.queue.add(element);
    }

    public List<T> list() {
        return new ArrayList<>(this.queue);
    }
}
