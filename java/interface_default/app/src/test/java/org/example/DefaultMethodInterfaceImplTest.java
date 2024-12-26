package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMethodInterfaceImplTest {
    @Test
    void shouldNotBeTheSameArray() {
        var a = new DefaultMethodInterfaceImpl();
        var b = new DefaultMethodInterfaceImpl();

        assertFalse(a.list().equals(b.list()));
    }
}
