package org.example.concurrency;

import java.util.concurrent.CountDownLatch;

/**
 * Ref: https://leetcode.cn/problems/print-in-order/description/
 */
class PrintInOrder {

    private CountDownLatch latch0 = new CountDownLatch(1);
    private CountDownLatch latch1 = new CountDownLatch(1);

    public PrintInOrder() {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        latch0.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        latch0.await();
        printSecond.run();
        latch1.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        latch1.await();
        printThird.run();
    }
}

