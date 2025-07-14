package org.example;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadAdder {
    private static final int WORKER_COUNT = 8;

    public int addWithLock(int limit) {
        final var cnt = new int[]{0};

        var counter = new AtomicInteger(0);
        Lock lock = new ReentrantLock();

        try (var service = Executors.newCachedThreadPool()) {
            List<Future> futures = new ArrayList<>();
            for (int i = 0; i < WORKER_COUNT; i++) {
                Future future = service.submit(() -> {
                    while (true) {
                        lock.lock();
                        if (cnt[0] >= limit) {
                            lock.unlock();
                            break;
                        }
                        cnt[0]++;
                        lock.unlock();
                    }
                });

                futures.add(future);
            }

            for (Future f: futures) {
                f.get();
            }
        } catch (ExecutionException | InterruptedException e ) {
            throw new RuntimeException(e);
        }

        return cnt[0];
    }

    public int add(int limit) {
        final var cnt = new int[]{0};

        var counter = new AtomicInteger(0);

        try (var service = Executors.newCachedThreadPool()) {
            List<Future> futures = new ArrayList<>();
            for (int i = 0; i < WORKER_COUNT; i++) {
                Future future = service.submit(() -> {
                    while (true) {
                        var v = counter.get();
                        if (v >= limit) {
                            break;
                        }
                        counter.compareAndSet(v, v+1);
                    }
                });

                futures.add(future);
            }

            for (Future f: futures) {
                f.get();
            }
        } catch (ExecutionException | InterruptedException e ) {
            throw new RuntimeException(e);
        }

        return counter.get();
    }
}
