package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
public class SimpleBenchmarkTest {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void stringConcatenation(Blackhole bh) {
        String result = "Hello" + " " + "World";
        bh.consume(result);
    }

    @Benchmark
    public void stringBuilder(Blackhole bh) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello").append(" ").append("World");
        bh.consume(sb.toString());
    }

    @Benchmark
    public void fibonacci(Blackhole bh) {
        bh.consume(fib(10));
    }

    private int fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }
}