package org.example.zerocopy;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class ZeroCopyBenchmark {

    private Path tempFile;
    private static final int PORT_TRADITIONAL = 19001;
    private static final int PORT_ZEROCOPY = 19002;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        // Create 10MB test file
        tempFile = Files.createTempFile("benchmark", ".dat");
        byte[] data = new byte[10 * 1024 * 1024]; // 10MB
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 256);
        }
        Files.write(tempFile, data);
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Benchmark
    public void traditionalFileTransfer() throws Exception {
        ZeroCopyExample server = new ZeroCopyExample();
        server.start(PORT_TRADITIONAL, tempFile, false);

        // Wait for server to be ready
        Thread.sleep(100);

        try {
            TestClient client = new TestClient();
            client.connect("localhost", PORT_TRADITIONAL);
            byte[] data = client.receiveFile();
        } finally {
            server.stop();
        }
    }

    @Benchmark
    public void zeroCopyFileTransfer() throws Exception {
        ZeroCopyExample server = new ZeroCopyExample();
        server.start(PORT_ZEROCOPY, tempFile, true);

        // Wait for server to be ready
        Thread.sleep(100);

        try {
            TestClient client = new TestClient();
            client.connect("localhost", PORT_ZEROCOPY);
            byte[] data = client.receiveFile();
        } finally {
            server.stop();
        }
    }
}
