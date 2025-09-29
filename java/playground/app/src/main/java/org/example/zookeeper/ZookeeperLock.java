package org.example.zookeeper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZookeeperLock implements Lock {
    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

     /*

     zk.create()
1. zk.getChildren

when lock is created ,a path should exist in zk

- [ ] when lock is acquired by 1 worker, other can not aquire it.
- [ ] when lock is acquired by 1 worker and it dead, a lock should be released
- [ ] when lck is released, other workers should be able to aquired it.
- [ ] correctness test
    - [ ] when 5 workers aquired lokc and append outputList once, the list should have 5 records.



     */
}
