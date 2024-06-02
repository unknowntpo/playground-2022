package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonTest {
    @Test
    public void testAdd() {
        Calculator mockCalculator = mock(Calculator.class);
        when(mockCalculator.add(2,3)).thenReturn(4);
        Person person = new Person(mockCalculator);

        assertEquals(4, person.add(2,3));
    }
}
