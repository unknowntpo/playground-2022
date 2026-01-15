package org.example.concurrency;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FizzBuzzTest {

    @Test
    void testFizzbuzz() throws InterruptedException {
        var fizzBuzz = new FizzBuzz(15);
        // Thread-safe list since multiple threads write to it
        var buf = Collections.synchronizedList(new ArrayList<String>());

        var t1 = new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> buf.add("fizz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        var t2 = new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> buf.add("buzz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        var t3 = new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> buf.add("fizzbuzz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        var t4 = new Thread(() -> {
            try {
                fizzBuzz.number((i) -> buf.add(String.format("%d", i)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        var threads = List.of(t1, t2, t3, t4);
        threads.forEach(Thread::start);

        // Wait for all threads to finish
        for (var t : threads) {
            t.join();
        }

        String want = "1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz";
        assertEquals(want, String.join(", ", buf));
    }
}