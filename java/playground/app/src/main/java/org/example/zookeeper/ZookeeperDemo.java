package org.example.zookeeper;

public class ZookeeperDemo {
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
