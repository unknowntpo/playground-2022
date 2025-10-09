package org.example.rate_limit;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;

import java.io.IOException;

public class SimpleThroughputAnalyzer {
    
    public static void analyzeFromCSV(String csvFilePath) throws IOException {
        System.out.println("Reading CSV: " + csvFilePath);
        Table data = Table.read().csv(csvFilePath);
        System.out.println("CSV loaded. Rows: " + data.rowCount() + ", Columns: " + data.columnNames());
        
        if (data.rowCount() == 0) {
            System.out.println("No data to analyze!");
            return;
        }
        
        // Convert nanoseconds to milliseconds for better readability
        DoubleColumn timeMs = data.longColumn("start_nanosecond")
            .asDoubleColumn()
            .subtract(data.longColumn("start_nanosecond").get(0).doubleValue())
            .divide(1_000_000.0)
            .setName("Time (ms)");
        
        data = data.addColumns(timeMs);
        
        // Calculate statistics
        printDetailedStats(data);
        printDataSample(data);
    }
    
    private static void printDetailedStats(Table data) {
        System.out.println("\n=== THROUGHPUT ANALYSIS ===");
        System.out.printf("Total measurements: %d\n", data.rowCount());
        
        // Calculate totals manually
        double totalTokens = 0;
        double maxCount = 0;
        double minCount = Double.MAX_VALUE;
        
        for (int i = 0; i < data.rowCount(); i++) {
            Number countValue = (Number) data.column("count").get(i);
            double count = countValue.doubleValue();
            totalTokens += count;
            maxCount = Math.max(maxCount, count);
            minCount = Math.min(minCount, count);
        }
        
        double testDurationMs = data.doubleColumn("Time (ms)").max();
        double testDurationSec = testDurationMs / 1000.0;
        double avgThroughput = totalTokens / testDurationSec;
        
        // Each measurement is 100ms, so convert to tokens/second
        double maxThroughputPerSec = maxCount * 10.0;
        double minThroughputPerSec = minCount * 10.0;
        
        System.out.printf("Total tokens: %.0f\n", totalTokens);
        System.out.printf("Test duration: %.3f seconds\n", testDurationSec);
        System.out.printf("Average throughput: %.2f tokens/second\n", avgThroughput);
        System.out.printf("Peak measurement: %.0f tokens in 100ms (%.0f tokens/sec)\n", maxCount, maxThroughputPerSec);
        System.out.printf("Min measurement: %.0f tokens in 100ms (%.0f tokens/sec)\n", minCount, minThroughputPerSec);
        System.out.println("========================\n");
    }
    
    private static void printDataSample(Table data) {
        System.out.println("=== DATA SAMPLE ===");
        System.out.println("Time (ms) | Tokens | Rate (tokens/sec)");
        System.out.println("----------|--------|------------------");
        
        int sampleSize = Math.min(10, data.rowCount());
        for (int i = 0; i < sampleSize; i++) {
            double timeMs = data.doubleColumn("Time (ms)").get(i);
            Number countValue = (Number) data.column("count").get(i);
            double count = countValue.doubleValue();
            double rate = count * 10.0; // 100ms measurement * 10 = tokens/sec
            
            System.out.printf("%8.1f | %6.0f | %16.1f\n", timeMs, count, rate);
        }
        
        if (data.rowCount() > sampleSize) {
            System.out.println("... (" + (data.rowCount() - sampleSize) + " more rows)");
        }
        System.out.println("===================\n");
    }
}