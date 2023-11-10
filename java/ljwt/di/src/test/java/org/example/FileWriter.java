package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.example.IWriter;


class FileWriter implements IWriter {
    private OutputStream outputStream;

    public FileWriter(String filename) throws IOException {
        this.outputStream = new FileOutputStream(filename);
    }

    @Override
    public void write(byte[] b) throws IOException  {
        this.outputStream.write(toString().getBytes());
    }
}
