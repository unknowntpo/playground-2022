package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class BestTimeToBuyAnsSellStock121Test extends TestCase {
    private final String name;
    private final int want;
    private final int[] prices;

    public BestTimeToBuyAnsSellStock121Test(String name, int[] prices, int want) {
        this.name = name;
        this.prices = prices;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"one", new int[]{1}, 0},
                new Object[]{"two - profit > 0", new int[]{ 1, 2 }, 1},
                new Object[]{"two - profit < 0", new int[]{ 2, 1 }, 0},
                new Object[]{"low high low", new int[]{ 1, 10, 7 }, 9},
                new Object[]{"low high high", new int[]{ 1, 2, 10, 20 }, 19},
                new Object[]{"high low low", new int[]{ 10, 7, 2 }, 0},
                new Object[]{"high low high", new int[]{ 10, 7, 12 }, 5},
                new Object[]{"two mountains", new int[]{ 2, 7, 3, 1, 10, 7 }, 9}
        );
    }

    @Test
    public void test() {
        BestTimeToBuyAndSellStock121.Solution solution = new BestTimeToBuyAndSellStock121.Solution();
        Map<String, Function<int[], Integer>> fns = Map.of(
                "solution::maxProfit", solution::maxProfit
                );
        fns.forEach((methodName, method) -> {
            System.out.printf("Calling method %s\n", methodName);
            int got = method.apply(prices);
            assertEquals(want, got);
        });
    }
}
