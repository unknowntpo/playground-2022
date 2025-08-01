package org.example;

import org.openjdk.jmh.annotations.Benchmark;

import java.math.BigInteger;

public class FibonacciCalculator implements FibCalculator {
    @Cache
    public BigInteger fib(int n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        return fib(n-1).add(fib(n-2));
    }
}
