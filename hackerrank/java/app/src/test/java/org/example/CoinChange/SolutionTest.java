package org.example.CoinChange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
    public static Stream<Arguments> provideCoinChange() {
        return Stream.of(
                Arguments.of(4, List.of(1L, 2L, 3L), 4L),
                Arguments.of(3, List.of(8L, 3L, 1L, 2L), 3L),
                Arguments.of(4, List.of(2L, 3L, 4L), 2L),
                Arguments.of(10, List.of(2L, 5L, 3L, 6L), 5L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCoinChange")
    public void testCoinChange(int n, List<Long> coins, long expected) {
        assertEquals(expected, Result.getWays(n, coins));
    }
}
