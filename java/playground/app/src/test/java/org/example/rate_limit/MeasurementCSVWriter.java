package org.example.rate_limit;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MeasurementCSVWriter {
    
    public static void writeMeasurementsToCSV(List<Measurement> measurements, String filename) throws IOException {
        // Ensure output directory exists
        new java.io.File("output").mkdirs();
        
        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.write("start_nanosecond,count\n");
            
            // Write each measurement
            for (Measurement measurement : measurements) {
                writer.write(String.format("%d,%d\n", 
                    measurement.getStartNanosecond(), 
                    measurement.getCount()));
            }
        }
        
        System.out.println("CSV written to: " + filename);
    }
}