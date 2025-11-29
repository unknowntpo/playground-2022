package org.example.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
    private ServerSocketChannel serverSocketChannel;

    public ZeroCopyExample() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void start(int port, Path sourceFile, boolean useZeroCopy) {
        this.executor.submit(() -> {
            this.running.set(true);

            try {
                if (useZeroCopy) {
                    // Use NIO ServerSocketChannel for true zero-copy
                    serverSocketChannel = ServerSocketChannel.open();
                    serverSocketChannel.bind(new InetSocketAddress(port));
                } else {
                    // Use traditional ServerSocket for buffered copy
                    serverSocket = new ServerSocket(port);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (running.get()) {
                try {
                    if (useZeroCopy) {
                        // Accept with SocketChannel for zero-copy
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        this.sendFileWithZeroCopy(sourceFile, clientChannel);
                        clientChannel.close();
                    } else {
                        // Accept with Socket for traditional
                        Socket clientSocket = serverSocket.accept();
                        this.sendFileWithoutZeroCopy(sourceFile, clientSocket);
                        clientSocket.close();
                    }
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
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
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
        try (var in = new FileInputStream(sourceFile.toFile());
             var out = new DataOutputStream(clientSocket.getOutputStream())) {

            // Send file size first (length-prefix protocol)
            long fileSize = sourceFile.toFile().length();
            out.writeLong(fileSize);

            // Manual buffer copy - FORCES user-space copying
            // File -> buffer (kernel -> user space)
            // buffer -> socket (user space -> kernel)
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void sendFileWithZeroCopy(Path sourceFile, SocketChannel clientChannel) throws IOException {
        try (var fileChannel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
            // Send file size first (length-prefix protocol)
            long fileSize = fileChannel.size();
            ByteBuffer sizeBuffer = ByteBuffer.allocate(8);
            sizeBuffer.putLong(fileSize);
            sizeBuffer.flip();

            // Write size prefix
            while (sizeBuffer.hasRemaining()) {
                clientChannel.write(sizeBuffer);
            }

            // TRUE zero-copy: direct FileChannel -> SocketChannel
            // Uses sendfile() syscall - data never enters user space
            long position = 0;
            while (position < fileSize) {
                position += fileChannel.transferTo(position, fileSize - position, clientChannel);
            }
        }
    }
}
