package org.example.zerocopy;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZeroCopyExampleTest {

    @TempDir
    Path tempDir;

    @ParameterizedTest
    @CsvSource({"false", "true"})
    void testFileTransfer(boolean useZeroCopy) throws Exception {
        String expectedContent = "Hello File Transfer!";
        int port = 18090;

        // Create test file
        Path sourceFile = tempDir.resolve("source.txt");
        Files.writeString(sourceFile, expectedContent);

        var server = new ZeroCopyExample();

        // Start server
        server.start(port, sourceFile, useZeroCopy);
        Thread.sleep(100); // Wait for server to be ready

        TestClient client = new TestClient();

        try {
            client.connect("localhost", port);
            byte[] fileBytes = client.receiveFile();
            assertEquals(expectedContent, new String(fileBytes));
        } finally {
            client.close();
            server.stop();
        }
    }
}