package org.example.legacy_code;

import java.time.LocalTime;

public class GreetingService {
    private final Clock clock;

    public GreetingService(Clock clock) {
        this.clock = clock;
    }

    public String greet() {
        LocalTime noon = LocalTime.parse("12:00:00");
        var now = clock.getLocalTime();

        if (now.isBefore(noon)) {
            return "Good morning";
        }
        return "Hello";
    }
}
