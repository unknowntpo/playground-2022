package org.example;

public class DefaultMethodInterfaceImpl implements DefaultMethodInterface {
    @Override
    public Integer[] list() {
        return DefaultMethodInterface.super.list();
    }
}
