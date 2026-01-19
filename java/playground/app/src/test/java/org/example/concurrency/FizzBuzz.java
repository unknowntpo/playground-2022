package org.example.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.IntConsumer;

/**
 * https://leetcode.cn/problems/fizz-buzz-multithreaded/
 * <p>
 * No separate process thread - each thread self-determines if it's their turn.
 */
class FizzBuzz {
    private static final Logger logger = LoggerFactory.getLogger(FizzBuzz.class);

    private final int n;
    private CyclicBarrier barrier;

    public FizzBuzz(int n) {
        this.n = n;
        this.barrier = new CyclicBarrier(4);
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException, BrokenBarrierException {
        for (int i = 1; i <= n; i++) {
            if (isFizzbuzz(i)) {
                printFizzBuzz.run();
            }
            barrier.await();
        }
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException, BrokenBarrierException {
        for (int i = 1; i <= n; i++) {
            if (isFizzOnly(i)) {
                printFizz.run();
            }
            barrier.await();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException, BrokenBarrierException {
        for (int i = 1; i <= n; i++) {
            if (isBuzzOnly(i)) {
                printBuzz.run();
            }
            barrier.await();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException, BrokenBarrierException {
        for (int i = 1; i <= n; i++) {
            if (isNumber(i)) {
                printNumber.accept(i);
            }
            barrier.await();
        }
    }

    // Helper methods to determine turn
    private boolean isFizzbuzz(int i) {
        return i % 3 == 0 && i % 5 == 0;
    }

    private boolean isFizzOnly(int i) {
        return i % 3 == 0 && i % 5 != 0;
    }

    private boolean isBuzzOnly(int i) {
        return i % 3 != 0 && i % 5 == 0;
    }

    private boolean isNumber(int i) {
        return i % 3 != 0 && i % 5 != 0;
    }
}