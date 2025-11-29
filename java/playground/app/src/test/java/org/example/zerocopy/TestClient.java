package org.example.zerocopy;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TestClient {
    private Socket socket;

    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    /**
     * Receive file from tcp socket using length-prefix protocol
     */
    public byte[] receiveFile() throws IOException {
        var in = new DataInputStream(socket.getInputStream());

        // Read 8-byte file size first (length-prefix)
        long fileSize = in.readLong();

        // Read exact file bytes
        byte[] fileData = new byte[(int) fileSize];
        in.readFully(fileData);

        return fileData;
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
