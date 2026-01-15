package org.example.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * https://leetcode.cn/problems/fizz-buzz-multithreaded/
 *
 * No separate process thread - each thread self-determines if it's their turn.
 */
class FizzBuzz {
    private static final Logger logger = LoggerFactory.getLogger(FizzBuzz.class);

    private final int n;
    private final ReentrantLock lock;
    private final Condition nextTurn;
    private int current;

    public FizzBuzz(int n) {
        this.n = n;
        this.lock = new ReentrantLock();
        this.current = 1;
        this.nextTurn = this.lock.newCondition();
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        /**
         *

         # will someone never got lock ?

         for fizz:
             acquire lock -> got current -> if >=n then return -> is fizz -> y -> print -> incr current -> release
                                    -> n -> wait for next state_change




             */
        while (true) {
            lock.lock();
            try {
                while (!isFizzbuzz(current)) {
                    if (current >= n) return;
                    this.nextTurn.await();
                }
                logger.info("fizzbuzz: {}", current);
                printFizzBuzz.run();
                current++;
                this.nextTurn.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (!isFizzOnly(current)) {
                    if (current >= n) return;
                    this.nextTurn.await();
                }
                logger.info("fizz: {}", current);
                printFizz.run();
                current++;
                this.nextTurn.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (!isBuzzOnly(current)) {
                    if (current >= n) return;
                    this.nextTurn.await();
                }
                logger.info("buzz: {}", current);
                printBuzz.run();
                current++;
                this.nextTurn.signalAll();
            } finally {
                lock.unlock();
            }
        }


    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (!isNumber(current)) {
                    if (current >= n) return;
                    this.nextTurn.await();
                }
                logger.info("number: {}", current);
                printNumber.accept(current);
                current++;
                this.nextTurn.signalAll();
            } finally {
                lock.unlock();
            }
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