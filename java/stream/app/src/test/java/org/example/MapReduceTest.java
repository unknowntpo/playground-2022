package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapReduceTest {

    @Test
    void testStreamInt() {
        List<Integer> numbers = Arrays.asList(1, 3, 4, 5, 6, 7, 8);
        var evenNums = numbers.stream().filter(e -> e % 2 != 0).collect(Collectors.toList());
        assertEquals(evenNums, Arrays.asList(1, 3, 5, 7));
    }
}
