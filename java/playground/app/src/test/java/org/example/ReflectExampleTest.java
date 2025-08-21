package org.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ReflectExampleTest {

    @Test
    void testGetFieldA() throws IllegalAccessException {
        int expect = 100;
        var example = new ReflectExample(expect, 0);
        Field fieldA = Arrays.stream(example.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals("a"))
                .findFirst().orElseThrow(RuntimeException::new);
        fieldA.setAccessible(true);
        assertEquals(expect, fieldA.getInt(example));
    }
}
