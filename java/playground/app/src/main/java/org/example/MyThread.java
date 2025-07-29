package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyThread extends Thread {
    private final int repeat;
    private final Queue<String> queue;

    public MyThread(Queue<String> queue, int repeat) {
        this.repeat = repeat;
        this.queue = queue;
    }

    @Override
    public void run() {
        var threads = Stream.generate(() -> new HelloThread(queue, 1)).limit(repeat).toList();
//        var threads = Collections.nCopies(repeat, new HelloThread(queue, 1));

        for (int i = 0; i < repeat; i++) {
            System.out.printf("starting thread %d\n", i);
            var t = threads.get(i);
            t.start();
        }

        threads.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public class HelloThread extends Thread {
        private final Queue<String> queue;
        private final int repeat;

        public HelloThread(Queue<String> queue, int repeat) {
            this.queue = queue;
            this.repeat = repeat;
        }

        @Override
        public void run() {
            System.out.printf("thread %s started\n", String.format("thread-%d", new Random().nextInt()));
            for (int i = 0; i < repeat; i++) {
                queue.add("Hello");
            }
        }
    }
}
