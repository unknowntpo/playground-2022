package org.example;

public interface DefaultMethodInterface {
    default Integer[] list() {
        return new Integer[]{};
    }
}
