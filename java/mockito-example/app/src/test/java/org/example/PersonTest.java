package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonTest {
    @Test
    public void testAdd() {
        Calculator mockCalculator = mock(Calculator.class);
        when(mockCalculator.add(2,3)).thenReturn(4);
        Person person = new Person(mockCalculator);

        assertEquals(4, person.add(2,3));
    }
    @Test
    public void testAddThreeTimes() {
        Calculator mockCalculator = mock(Calculator.class);
        Person person = new Person(mockCalculator);
        final int CALL_TIMES = 3;
        for (int i = 0 ; i < CALL_TIMES ; i++) {
            person.add(2,3);
        }
        // assert add is called three times
        verify(mockCalculator, times(CALL_TIMES)).add(2,3);
    }
}
