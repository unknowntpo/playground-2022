package org.example;

public class MyObject {
    int x;
    int y;

    public MyObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "MyObject{x=" + x + ", y=" + y + "}";
    }
}
