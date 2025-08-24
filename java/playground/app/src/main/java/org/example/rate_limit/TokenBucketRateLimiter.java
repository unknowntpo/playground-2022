package org.example.rate_limit;

import java.time.Instant;
import java.util.Optional;

public class TokenBucketRateLimiter {
    private int capacity;

    public TokenBucketRateLimiter(int capacity) {
        this.capacity = capacity;
    }

    public Optional<Long> getToken() {
        if (capacity > 0) {
            capacity--;
            return Optional.of(System.currentTimeMillis() / 1000L);
        }
        return Optional.empty();
    }

    public boolean hasToken() {
        return capacity > 0;
    }
}
