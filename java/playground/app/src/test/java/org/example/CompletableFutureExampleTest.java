package org.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompletableFutureExampleTest {
    private static Logger log = LoggerFactory.getLogger(CompletableFutureExampleTest.class);

    @Test
    void testCompletableFuture() {
        var counter = new AtomicInteger();

        int COUNT = 3;

        var futures = IntStream.range(0, COUNT).mapToObj(i -> CompletableFuture.supplyAsync(() -> {
            counter.incrementAndGet();
            return null;
        })).collect(Collectors.toCollection(ArrayList::new));

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        assertEquals(COUNT, counter.get());
    }

    @Test
    void testCancellableWorkers() throws InterruptedException {
        var counter = new AtomicInteger();
        var cancelled = new AtomicBoolean(false);

        int WORKER_COUNT = 3;

        var futures = IntStream.range(0, WORKER_COUNT)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    while (!cancelled.get()) {
                        counter.incrementAndGet();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    return null;
                }))
                .collect(Collectors.toCollection(ArrayList::new));

        Thread.sleep(500);

        futures.forEach(f -> f.cancel(false));

        log.info("Final counter value: {}", counter.get());
    }

    @Test
    void testThreadCancellation() throws InterruptedException {
        var helloThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Hello");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("I am interrupted");
                    break;
                }
            }
        });
        helloThread.start();

        Thread.sleep(300);

        System.out.println("main thread interrupt hello thread");
        helloThread.interrupt();
        assertEquals(1, 1 + 1 - 1);
    }

    @Test
    void testVirtualThread() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

        var future = CompletableFuture.supplyAsync(()-> {
            System.out.println("Hello");
            return null;
        }, service);
        future.get();
    }
}
