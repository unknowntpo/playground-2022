package org.example.legacy_code;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceTest {
    @Test
    void test_greeting_afternoon() {
        LocalTime now = LocalTime.parse("14:00:00");
        var service = new GreetingService(new Clock(now));
        assertEquals("Hello", service.greet());
    }

    @Test
    void test_greeting_morning() {
        LocalTime now = LocalTime.parse("07:00:00");
        var service = new GreetingService(new Clock(now));
        assertEquals("Good morning", service.greet());
    }

//    public class GreetingServiceForTesting extends GreetingService {
//        private final LocalTime now;
//
//        public GreetingServiceForTesting(LocalTime now) {
//            super();
//            this.now = now;
//        }
//
//        @Override
//        protected LocalTime getLocalTime() {
//            return this.now;
//        }
//    }
}
