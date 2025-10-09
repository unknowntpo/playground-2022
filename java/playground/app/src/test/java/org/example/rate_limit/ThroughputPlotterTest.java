package org.example.rate_limit;

import org.junit.jupiter.api.Test;
import java.io.IOException;

public class ThroughputPlotterTest {
    
    @Test 
    void testPlotThroughputFromCSV() throws IOException {
        // Assuming you have a CSV file from your throughput test
        String csvFile = "output/throughput_test.csv";
        String htmlOutput = "output/throughput_analysis.html";
        
        // Check if CSV exists (your main test should generate this)
        if (java.nio.file.Files.exists(java.nio.file.Path.of(csvFile))) {
            ThroughputPlotter.plotThroughputFromCSV(csvFile, htmlOutput);
            System.out.println("Open " + htmlOutput + " in your browser to view the plots!");
        } else {
            System.out.println("CSV file not found: " + csvFile);
            System.out.println("Run your TokenBucketRateLimiterThroughputTest first to generate data.");
        }
    }
}