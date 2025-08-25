package org.example.rate_limit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketRateLimiterThroughputTest {
    @Test
    void measureThroughput() throws IOException {
        var timestampQueue = new ConcurrentLinkedQueue<Long>();
        int capacity = 100;
        // assume each request takes 10ms to complete
        var workDuration = 10;
        var limiter = new TokenBucketRateLimiter(capacity);
        var done = new AtomicBoolean(false);
        for (int i = 0; i < 8; i++) {
            CompletableFuture.runAsync(()-> {
                while (!done.get())  {
                    try {
                        long tstmp = limiter.getTokenSync();
                        timestampQueue.add(tstmp);
                        Thread.sleep(workDuration);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        limiter.releaseToken();
                    }
                }
            });
        }

        var measurements = new ArrayList<Measurement>();

        CompletableFuture.runAsync(()-> {
            // count measurement
            // 1ms duration for each measurement
            long duration = 1_000_000;
            long startNanosecond;
            while (!done.get() || !timestampQueue.isEmpty())  {
                startNanosecond = timestampQueue.poll();
                var measurement = new Measurement(duration);
                measurement.startAtNanoSecond(startNanosecond);
                // timestampQueue is in nanoseocond
                while (!timestampQueue.isEmpty() && timestampQueue.peek() - startNanosecond < duration) {
                    measurement.addOne();
                }
                measurements.add(measurement);
            }
        });

        var testDurationMs = 3000;
        try {
            Thread.sleep(testDurationMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        MeasurementCSVWriter.writeMeasurementsToCSV(measurements, "output/throughput_test.csv");

        // start 8 threads to run concurrent requests.
        // put all tstmp in to measurements, each has 10 ms window
        // [] [] [] []
        // write measurement into csv.
    }
}
