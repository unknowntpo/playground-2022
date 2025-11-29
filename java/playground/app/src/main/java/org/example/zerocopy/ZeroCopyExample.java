package org.example.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZeroCopyExample {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService executor;
    private ServerSocket serverSocket;

    public ZeroCopyExample() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void start(int port, Path sourceFile, boolean useZeroCopy) {
        this.executor.submit(() -> {
            this.running.set(true);
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Server listening on port " + port);
            while (running.get()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

                    if (useZeroCopy) {
                        this.sendFileWithZeroCopy(sourceFile, clientSocket);
                    } else {
                        this.sendFileWithoutZeroCopy(sourceFile, clientSocket);
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void stop() {
        this.running.set(false);

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Executor did not terminate");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for shutdown", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFileWithoutZeroCopy(Path sourceFile, Socket clientSocket) throws IOException {
        try (var in = new FileInputStream(String.valueOf(sourceFile))) {
            var out = clientSocket.getOutputStream();
            in.transferTo(out);
        }
    }

    private void sendFileWithZeroCopy(Path sourceFile, Socket clientSocket) throws IOException {
        try (var fileChannel = FileChannel.open(sourceFile, StandardOpenOption.READ);
             var socketChannel = Channels.newChannel(clientSocket.getOutputStream())) {

            long fileSize = fileChannel.size();
            long position = 0;

            while (position < fileSize) {
                position += fileChannel.transferTo(position, fileSize - position, socketChannel);
            }
        }
    }
}
