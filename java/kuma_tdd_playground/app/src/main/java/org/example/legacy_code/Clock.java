package org.example.legacy_code;

import java.time.LocalTime;

public class Clock {
    private final LocalTime now;

    public Clock(LocalTime now) {
        this.now = now;
    }

    protected LocalTime getLocalTime() {
        return now;
    }
}
