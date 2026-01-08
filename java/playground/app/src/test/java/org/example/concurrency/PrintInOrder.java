package org.example.concurrency;

/**
 * Ref: https://leetcode.cn/problems/print-in-order/description/
 */
class PrintInOrder {

    private Integer value = 0;

    public PrintInOrder() {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        while (true) {
            synchronized (this.value) {
                if (value == 0) {
                    printFirst.run();
                    value += 1;
                    break;
                }
            }
            Thread.sleep(1);
        }
        // printFirst.run() outputs "first". Do not change or remove this line.
//        printFirst.run();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (true) {
            synchronized (this.value) {
                if (value == 1) {
                    printSecond.run();
                    value += 1;
                    break;
                }
            }
            Thread.sleep(1);
        }
        // printSecond.run() outputs "second". Do not change or remove this line.
//        printSecond.run();
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (true) {
            synchronized (this.value) {
                if (value == 2) {
                    printThird.run();
                    value += 1;
                    break;
                }
            }
            Thread.sleep(1);
        }

        // printThird.run() outputs "third". Do not change or remove this line.
//        printThird.run();
    }
}

