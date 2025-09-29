package org.example.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZookeeperDemoTest {
    ZooKeeper zk;

    @BeforeAll
    public void setUp() throws IOException {
        zk = new ZooKeeper("localhost:2181", 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Event: " + event);
            }
        });
    }

    @BeforeEach
    void clean() throws InterruptedException, KeeperException {
        try {
            zk.delete("/demo", -1);
        } catch (KeeperException.NoNodeException e) {
            // Node doesn't exist, that's fine
        }
    }

    @Test
    void basic() throws InterruptedException, KeeperException {
        String data = "hello";
        zk.create("/demo", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        var gotData = zk.getData("/demo", null, null);

        assertEquals(data, new String(gotData));
    }
}