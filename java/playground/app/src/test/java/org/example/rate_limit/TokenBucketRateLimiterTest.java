package org.example.rate_limit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {
    @Test
    void testGetToken() {
        int cap = 10;
        var limiter = new TokenBucketRateLimiter(10);
        for (int i = 0; i < cap; i++) {
            assertTrue(limiter.getToken().isPresent());
        }
        assertTrue(limiter.getToken().isEmpty());
        assertFalse(limiter.hasToken());
    }
}
