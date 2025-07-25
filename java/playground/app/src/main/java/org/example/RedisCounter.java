package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisCounter {
    private final RedissonClient redisson;
    private final RAtomicLong counter;
    
    public RedisCounter(String counterName) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String redisUrl = dotenv.get("REDIS_URL", "redis://localhost:6380");
        this.redisson = createRedissonClient(redisUrl);
        this.counter = redisson.getAtomicLong(counterName);
    }
    
    public RedisCounter(String counterName, String redisUrl) {
        this.redisson = createRedissonClient(redisUrl);
        this.counter = redisson.getAtomicLong(counterName);
    }
    
    private RedissonClient createRedissonClient(String redisUrl) {
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        return Redisson.create(config);
    }
    
    public long increment() {
        return counter.incrementAndGet();
    }
    
    public long decrement() {
        return counter.decrementAndGet();
    }
    
    public long addAndGet(long delta) {
        return counter.addAndGet(delta);
    }
    
    public long get() {
        return counter.get();
    }
    
    public void set(long newValue) {
        counter.set(newValue);
    }
    
    public boolean compareAndSet(long expect, long update) {
        return counter.compareAndSet(expect, update);
    }
    
    public void reset() {
        counter.set(0);
    }
    
    public void close() {
        if (redisson != null && !redisson.isShutdown()) {
            redisson.shutdown();
        }
    }
    
    public static void main(String[] args) {
        RedisCounter counter = new RedisCounter("test-counter");
        
        try {
            System.out.println("Initial count: " + counter.get());
            
            System.out.println("Increment: " + counter.increment());
            System.out.println("Increment: " + counter.increment());
            System.out.println("Add 5: " + counter.addAndGet(5));
            System.out.println("Current count: " + counter.get());
            
            System.out.println("Decrement: " + counter.decrement());
            System.out.println("Final count: " + counter.get());
            
            if (counter.compareAndSet(counter.get(), 100)) {
                System.out.println("Successfully set to 100: " + counter.get());
            }
            
        } finally {
            counter.close();
        }
    }
}
