package org.example.zookeeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZookeeperLockTest {
    /*


- [ ] when lock is acquired by 1 worker, other can not aquire it.
- [ ] when lock is acquired by 1 worker and it dead, a lock should be released
- [ ] when lck is released, other workers should be able to aquired it.
        - [ ] correctness test
- [ ] when 5 workers aquired lokc and append outputList once, the list should have 5 records.

     */

    @Test
    void testSingleClientLockAndUnLock() {
        // when lock is acquired by 1 worker, other can not aquire it.
        var zkLock = new ZookeeperLock();
        zkLock.lock();


    }
}