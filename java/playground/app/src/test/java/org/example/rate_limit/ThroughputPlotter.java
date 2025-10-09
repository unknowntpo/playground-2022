package org.example.rate_limit;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.api.ScatterPlot;

import java.io.File;
import java.io.IOException;

public class ThroughputPlotter {
    
    public static void plotThroughputFromCSV(String csvFilePath, String outputHtmlPath) throws IOException {
        // Read CSV file
        System.out.println("Reading CSV: " + csvFilePath);
        Table data = Table.read().csv(csvFilePath);
        System.out.println("CSV loaded. Rows: " + data.rowCount() + ", Columns: " + data.columnNames());
        
        // Convert nanoseconds to milliseconds for better readability
        // First convert LongColumn to DoubleColumn, then perform operations
        DoubleColumn timeMs = data.longColumn("start_nanosecond")
            .asDoubleColumn()
            .subtract(data.longColumn("start_nanosecond").get(0).doubleValue()) // Start from 0
            .divide(1_000_000.0) // Convert to milliseconds
            .setName("Time (ms)");
        
        // Add the time column to the table
        data = data.addColumns(timeMs);
        
        // Create line plot for throughput over time
        var throughputFigure = LinePlot.create(
            "Token Bucket Throughput Over Time",
            data,
            "Time (ms)",
            "count"
        );
        
        // Create scatter plot to show individual measurements
        var scatterFigure = ScatterPlot.create(
            "Throughput Measurements", 
            data,
            "Time (ms)", 
            "count"
        );
        
        // Calculate cumulative tokens  
        DoubleColumn cumulativeTokens = DoubleColumn.create("Cumulative Tokens");
        double cumulative = 0;
        for (int i = 0; i < data.rowCount(); i++) {
            // Handle count column which might be Long or Double
            Number countValue = (Number) data.column("count").get(i);
            cumulative += countValue.doubleValue();
            cumulativeTokens.append(cumulative);
        }
        data = data.addColumns(cumulativeTokens);
        
        // Create cumulative plot
        var cumulativeFigure = LinePlot.create(
            "Cumulative Tokens Over Time",
            data,
            "Time (ms)",
            "Cumulative Tokens"
        );
        
        // Calculate throughput rate (tokens per second)
        DoubleColumn throughputRate = DoubleColumn.create("Throughput Rate (tokens/sec)");
        for (int i = 0; i < data.rowCount(); i++) {
            // Each measurement is 100ms duration, so tokens/100ms * 10 = tokens/sec
            Number countValue = (Number) data.column("count").get(i);
            double rate = countValue.doubleValue() * 10.0; // 100ms * 10 = 1000ms = 1 second
            throughputRate.append(rate);
        }
        data = data.addColumns(throughputRate);
        
        // Create throughput rate plot
        var rateFigure = LinePlot.create(
            "Instantaneous Throughput Rate",
            data,
            "Time (ms)",
            "Throughput Rate (tokens/sec)"
        );
        
        // Generate SVG files
        String baseFileName = outputHtmlPath.replace(".html", "");
        new File(baseFileName).getParentFile().mkdirs();
        
        // Save each plot as SVG
        savePlotAsSVG(throughputFigure, baseFileName + "_throughput.svg");
        savePlotAsSVG(scatterFigure, baseFileName + "_scatter.svg");
        savePlotAsSVG(cumulativeFigure, baseFileName + "_cumulative.svg");
        savePlotAsSVG(rateFigure, baseFileName + "_rate.svg");
        
        System.out.println("Interactive plots saved:");
        System.out.println("  📈 " + baseFileName + "_throughput.html");
        System.out.println("  📊 " + baseFileName + "_scatter.html");
        System.out.println("  📈 " + baseFileName + "_cumulative.html");
        System.out.println("  📊 " + baseFileName + "_rate.html");
        printSummaryStats(data);
    }
    
    private static void savePlotAsSVG(tech.tablesaw.plotly.components.Figure figure, String fileName) throws IOException {
        // Show the plot (which will open in browser, or save manually)
        // Unfortunately Tablesaw 0.43.0 doesn't have direct SVG export
        // For now, we'll use HTML export instead
        String htmlContent = figure.asJavascript("plot-div");
        String svgHtml = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
            </head>
            <body>
                <div id="plot-div"></div>
                <script>%s</script>
            </body>
            </html>
            """, htmlContent);
        
        // Save as HTML instead of SVG
        String htmlFileName = fileName.replace(".svg", ".html");
        java.nio.file.Files.writeString(java.nio.file.Path.of(htmlFileName), svgHtml);
    }
    
    private static void printSummaryStats(Table data) {
        System.out.println("\n=== THROUGHPUT ANALYSIS ===");
        System.out.printf("Total measurements: %d\n", data.rowCount());
        
        // Calculate totals manually since count might be LongColumn
        double totalTokens = 0;
        for (int i = 0; i < data.rowCount(); i++) {
            Number countValue = (Number) data.column("count").get(i);
            totalTokens += countValue.doubleValue();
        }
        
        System.out.printf("Total tokens: %.0f\n", totalTokens);
        System.out.printf("Test duration: %.3f seconds\n", data.doubleColumn("Time (ms)").max() / 1000.0);
        System.out.printf("Average throughput: %.2f tokens/second\n", 
            totalTokens / (data.doubleColumn("Time (ms)").max() / 1000.0));
        System.out.printf("Peak throughput: %.0f tokens/second\n", 
            data.doubleColumn("Throughput Rate (tokens/sec)").max());
        System.out.printf("Min throughput: %.0f tokens/second\n", 
            data.doubleColumn("Throughput Rate (tokens/sec)").min());
        System.out.println("========================\n");
    }
}