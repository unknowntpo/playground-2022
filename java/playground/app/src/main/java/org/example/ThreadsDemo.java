package org.example;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadsDemo {
    /**
     * Starts `threadsCount` threads, each thread add `addCount` times, and return the final count.
     *
     * @param threadsCount
     * @param addCount
     * @return final count.
     */
    public static int multiThreadAdd(int threadsCount, int addCount) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        AtomicInteger cnt = new AtomicInteger(0);

        for (int i = 0; i < threadsCount; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < addCount; j++) {
                    cnt.incrementAndGet();
                }
            });
            threads.add(thread);
        }

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        return cnt.get();
    }
}
