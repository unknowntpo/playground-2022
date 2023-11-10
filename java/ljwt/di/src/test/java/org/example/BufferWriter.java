package org.example;

import org.example.IWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferWriter implements IWriter {
    private ByteArrayOutputStream buffer;

    public BufferWriter() {
        this.buffer = new ByteArrayOutputStream();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.buffer.write(b);
    }

    public byte[] toByteArray() {
        return buffer.toByteArray();
    }

    public String toString() {
        return buffer.toString();
    }


//    public BufferWriter() {
//        this.buffer = new ByteArrayOutputStream();
//    }
//
//    @Override
//    public void write(byte[] b) throws IOException {
//        buffer.write(b);
//    }
//
//    @Override
//    public void write(String s) throws IOException {
//        buffer.write(s.getBytes(StandardCharsets.UTF_8));
//    }
//
//    @Override
//    public void close() throws IOException {
//        buffer.close();
//    }
//
//    public byte[] toByteArray() {
//        return buffer.toByteArray();
//    }
}
