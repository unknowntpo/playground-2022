package org.example.zerocopy;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class ZeroCopyBenchmark {

    @State(Scope.Benchmark)
    public static class ServerState {
        Path tempFile;
        ZeroCopyExample traditionalServer;
        ZeroCopyExample zeroCopyServer;
        static final int PORT_TRADITIONAL = 19001;
        static final int PORT_ZEROCOPY = 19002;

        @Setup(Level.Trial)
        public void setupTrial() throws Exception {
            // Create 100MB test file
            tempFile = Files.createTempFile("benchmark", ".dat");
            byte[] data = new byte[100 * 1024 * 1024]; // 100MB
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (i % 256);
            }
            Files.write(tempFile, data);

            // Start servers once
            traditionalServer = new ZeroCopyExample();
            traditionalServer.start(PORT_TRADITIONAL, tempFile, false);

            zeroCopyServer = new ZeroCopyExample();
            zeroCopyServer.start(PORT_ZEROCOPY, tempFile, true);

            // Wait for servers to be ready
            Thread.sleep(200);
        }

        @TearDown(Level.Trial)
        public void tearDownTrial() throws IOException {
            if (traditionalServer != null) {
                traditionalServer.stop();
            }
            if (zeroCopyServer != null) {
                zeroCopyServer.stop();
            }
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
        }
    }

    @State(Scope.Thread)
    public static class ClientState {
        TestClient client;

        @Setup(Level.Invocation)
        public void setupClient() {
            client = new TestClient();
        }

        @TearDown(Level.Invocation)
        public void tearDownClient() throws IOException {
            if (client != null) {
                client.close();
            }
        }
    }

    @Benchmark
    public byte[] traditionalFileTransfer(ServerState serverState, ClientState clientState) throws Exception {
        clientState.client.connect("localhost", ServerState.PORT_TRADITIONAL);
        return clientState.client.receiveFile();
    }

    @Benchmark
    public byte[] zeroCopyFileTransfer(ServerState serverState, ClientState clientState) throws Exception {
        clientState.client.connect("localhost", ServerState.PORT_ZEROCOPY);
        return clientState.client.receiveFile();
    }
}
