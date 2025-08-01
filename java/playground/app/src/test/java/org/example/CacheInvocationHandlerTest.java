package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class CacheInvocationHandlerTest {
    private static void performanceTest(FibCalculator calc) {
        System.out.println("\n=== Performance Test ===");

        int n = 35;

        // Test uncached version (will be slow)
        System.out.println("Testing uncached fib(" + n + ")...");
        long start = System.currentTimeMillis();
        BigInteger res = calc.fib(n);
        long uncachedTime = System.currentTimeMillis() - start;

        System.out.printf("\nResults: %d, time elapsed: %d\n", res,  uncachedTime);

        CacheStorage.printStats();
    }

    @Test
    public void testCache() {
        // Using annotation processor - no more manual proxy creation!
//        FibCalculator calc = new FibonacciCalculatorCached(new FibonacciCalculator());
        FibCalculator calc = new FibonacciCalculator();

        // Test basic caching
        System.out.println("Testing automatic caching:");
        System.out.println("fib(10) = " + calc.fib(10));        // Computed and cached
        System.out.println("fib(10) = " + calc.fib(10));        // Retrieved from cache
        System.out.println("fib(15) = " + calc.fib(15));        // Uses cached fib(10) results

        performanceTest(calc);
    }
}
