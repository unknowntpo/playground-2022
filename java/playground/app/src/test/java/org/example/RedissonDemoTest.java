package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedissonDemoTest {

    private ReddisonDemo redissonDemo;

    @BeforeEach
    void setUp() {
        redissonDemo = new ReddisonDemo();
    }

    @AfterEach
    void tearDown() {
        redissonDemo.shutdown();
    }

    @Test
    void testSetAndGetString() {
        String key = "test:key1";
        String value = "test value";

        var a = ReddisonDemo.testVi.test();

        redissonDemo.setString(key, value);
        String retrievedValue = redissonDemo.getString(key);

        assertEquals(value, retrievedValue);
    }

    @Test
    void testGetNonExistentKey() {
        String key = "test:nonexistent";

        String result = redissonDemo.getString(key);

        assertNull(result);
    }

    @Test
    void testClientSideCache() {
        String key = "test:cache";
        String value = "cached value";

        redissonDemo.setString(key, value);

        // First call - should load from Redis
        String firstCall = redissonDemo.getString(key);
        assertEquals(value, firstCall);

        // Second call - should hit cache
        String secondCall = redissonDemo.getString(key);
        assertEquals(value, secondCall);
    }

    @Test
    void testCacheInvalidation() {
        String key = "test:invalidate";
        String value = "value to invalidate";

        redissonDemo.setString(key, value);
        redissonDemo.getString(key); // Load into cache

        redissonDemo.invalidateCache(key);

        // Should still get value from Redis after cache invalidation
        String result = redissonDemo.getString(key);
        assertEquals(value, result);
    }
}
