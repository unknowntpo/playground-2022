package org.example;


import java.io.IOException;

public class Greeter {
    private IWriter writer;
    public Greeter(IWriter writer) {
        this.writer = writer;
    }
    public void greet(String name) throws IOException {
         this.writer.write(("Hello, " + name).getBytes());
    }
}

//import java.io.IOException;
//
//public interface IWriter {
//    void write(byte[] b) throws IOException;
//    void write(String s) throws IOException;
//    void close() throws IOException;
//}