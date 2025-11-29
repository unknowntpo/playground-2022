package org.example.zerocopy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    private String host;
    private int port;

    public void connect(String host, int port) {
        this.host =  host;
        this.port = port;
    }

    /**
     * Recieve file from tcp socket
     */
    public byte[] receiveFile() throws IOException {
        try (var socket = new Socket(this.host, this.port)) {
            var in = socket.getInputStream();
            var baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
