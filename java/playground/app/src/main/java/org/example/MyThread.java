package org.example;

import java.util.List;

public class MyThread extends Thread {
    private List<String> list;

    public MyThread(List<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            list.add("Hello");
        }
    }
}
