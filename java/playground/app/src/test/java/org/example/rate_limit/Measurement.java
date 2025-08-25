package org.example.rate_limit;

public class Measurement {
    public long getStartNanosecond() {
        return startNanosecond;
    }

    // [startNanosecond, startNanosecond+durationNanosecond)
    private long startNanosecond;
    private final long durationNanosecond;
    private long cnt;

    public Measurement(long durationNanosecond) {
        this.durationNanosecond = durationNanosecond;
    }

    public void addOne() {
        this.cnt++;
    }

    public void startAtNanoSecond(long startNanosecond) {
        this.startNanosecond = startNanosecond;

    }

    public long getCount() {
        return cnt;
    }
}
