/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    public class ExampleCondition implements Callable<Boolean> {
        private int count = 0;
        private final int maxCount;

        public ExampleCondition(int maxCount) {
            this.maxCount = maxCount;
        }

        @Override
        public Boolean call() throws Exception {
            count++;
            System.out.println("Condition evaluated. Count: " + count);
            return count >= maxCount;
        }
    }

    @Test
    public void updatesCustomerStatus() throws Exception {
        // Awaitility lets you wait until the asynchronous operation completes:
        await().atMost(5, SECONDS).until(new ExampleCondition(3));
    }
}
