package org.example.rate_limit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketRateLimiterThroughputTest {
    @Test
    void measureThroughput() throws IOException {
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Common pool size: " + ForkJoinPool.commonPool().getParallelism());

        var timestampQueue = new ConcurrentLinkedQueue<Long>();
        int capacity = 100;
        // assume each request takes 10ms to complete
        var workDuration = 10;
        var limiter = new TokenBucketRateLimiter(capacity);
        var done = new AtomicBoolean(false);
        for (int i = 0; i < ForkJoinPool.commonPool().getParallelism() - 1; i++) {
            CompletableFuture.runAsync(() -> {
                while (!done.get()) {
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

        var testDurationMs = 3000;
        try {
            Thread.sleep(testDurationMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        done.set(true);

        try {
            // wait for all thread to stop
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Now process all timestamps into 100ms measurement windows
        var measurements = new ArrayList<Measurement>();
        long windowDuration = 100_000_000; // 100ms in nanoseconds

        if (!timestampQueue.isEmpty()) {
            // Get the first timestamp to determine start time
            Long firstTimestamp = timestampQueue.peek();
            long currentWindowStart = firstTimestamp;

            while (!timestampQueue.isEmpty()) {
                var measurement = new Measurement(windowDuration);
                measurement.startAtNanoSecond(currentWindowStart);

                // Count tokens in current 100ms window
                Long timestamp;
                while ((timestamp = timestampQueue.peek()) != null) {
                    // Check if token belongs to current window
                    if (timestamp >= currentWindowStart && timestamp < currentWindowStart + windowDuration) {
                        timestampQueue.poll(); // Remove it from queue
                        measurement.addOne();
                    } else {
                        // Token belongs to future window, stop processing this window
                        break;
                    }
                }

                measurements.add(measurement);

                // Move to next 100ms window
                currentWindowStart += windowDuration;
            }
        }

        MeasurementCSVWriter.writeMeasurementsToCSV(measurements, "output/throughput_test.csv");

        // Analyze throughput data (simpler approach due to Tablesaw plotting issues with Java 21)
        SimpleThroughputAnalyzer.analyzeFromCSV("output/throughput_test.csv");

        System.out.println("✅ Test completed!");
        System.out.println("📊 Data saved to: output/throughput_test.csv");
    }
}
