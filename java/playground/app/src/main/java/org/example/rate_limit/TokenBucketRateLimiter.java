package org.example.rate_limit;

import java.time.Instant;
import java.util.Optional;

public class TokenBucketRateLimiter {
    private int capacity;

    public TokenBucketRateLimiter(int capacity) {
        this.capacity = capacity;
    }

    public synchronized Optional<Long> getToken() {
        if (capacity > 0) {
            capacity--;
            return Optional.of(System.nanoTime());
        }
        return Optional.empty();
    }

    public synchronized boolean hasToken() {
        return capacity > 0;
    }

    public Long getTokenSync() throws InterruptedException {
        while (!hasToken()) {}
        return getToken().orElseThrow(); // Should always succeed since we checked hasToken()
    }

    public Long getTokenSync(long timeoutMs) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (!hasToken()) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                throw new RuntimeException("Timeout waiting for token after " + timeoutMs + "ms");
            }
            Thread.sleep(10);
        }
        return getToken().orElseThrow();
    }

    public synchronized void releaseToken() {
        capacity++;
    }
}
