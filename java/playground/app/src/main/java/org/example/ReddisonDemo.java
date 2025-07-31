package org.example;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.cdimascio.dotenv.Dotenv;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.time.Duration;

public class ReddisonDemo {
    private final RedissonClient redisson;
    private final Cache<String, String> clientCache;

    public ReddisonDemo() {
        // Initialize Redisson client
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        Config config = new Config();
        config.useSingleServer().setAddress(dotenv.get("REDIS_URL", "redis://localhost:6380"));

        this.redisson = Redisson.create(config);

        // Initialize client-side cache with Caffeine
        this.clientCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();
    }

    static class testVi {
        public static int test() {
            return 1;
        }
    }

    public void setString(String key, String value) {
        // Write to Redis
        redisson.getBucket(key).set(value);
        // Update client-side cache
        clientCache.put(key, value);
        System.out.println("Set " + key + " = " + value);
    }

    public String getString(String key) {
        // Try client-side cache first
        String cachedValue = clientCache.getIfPresent(key);
        if (cachedValue != null) {
            System.out.println("Cache hit for " + key + " = " + cachedValue);
            return cachedValue;
        }

        // Cache miss - fetch from Redis
        String value = (String) redisson.getBucket(key).get();
        if (value != null) {
            clientCache.put(key, value);
            System.out.println("Cache miss, loaded from Redis: " + key + " = " + value);
        } else {
            System.out.println("Key not found: " + key);
        }
        return value;
    }

    public void invalidateCache(String key) {
        clientCache.invalidate(key);
        System.out.println("Invalidated cache for key: " + key);
    }

    public void shutdown() {
        redisson.shutdown();
    }

    public static void main(String[] args) {
        ReddisonDemo demo = new ReddisonDemo();

        try {
            // Basic string operations with client-side caching
            demo.setString("user:1", "John Doe");
            demo.setString("user:2", "Jane Smith");

            // First read - cache miss
            demo.getString("user:1");

            // Second read - cache hit
            demo.getString("user:1");

            // Read different key - cache miss
            demo.getString("user:2");

            // Invalidate cache and read again
            demo.invalidateCache("user:1");
            demo.getString("user:1");

        } finally {
            demo.shutdown();
        }
    }
}
