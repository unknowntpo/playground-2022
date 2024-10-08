package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Calculator {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<Integer> addAsync(int x, int y) {
        return executor.submit(() -> {
            Thread.sleep(1000);
            return x + y;
        });
    }
}
