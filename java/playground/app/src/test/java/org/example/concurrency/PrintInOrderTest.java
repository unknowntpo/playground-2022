package org.example.concurrency;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintInOrderTest {
    @SneakyThrows
    @Test
     void testPrintInOrder() {
        var clazz = new PrintInOrder();
        var threads = new ArrayList<Thread>();
        var buf = new StringBuffer();
        threads.add(new Thread(() -> {
            try {
                clazz.first(() -> buf.append("first"));
            } catch (InterruptedException e) {
                // FIXME: when will this exception thrown ?
                throw new RuntimeException(e);
            }
        }));
        threads.add(new Thread(() -> {
            try {
                clazz.second(() -> buf.append("second"));
            } catch (InterruptedException e) {
                // FIXME: when will this exception thrown ?
                throw new RuntimeException(e);
            }
        }));
        threads.add(new Thread(() -> {
            try {
                clazz.third(() -> buf.append("third"));
            } catch (InterruptedException e) {
                // FIXME: when will this exception thrown ?
                throw new RuntimeException(e);
            }
        }));
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals("firstsecondthird", buf.toString());
    }


}