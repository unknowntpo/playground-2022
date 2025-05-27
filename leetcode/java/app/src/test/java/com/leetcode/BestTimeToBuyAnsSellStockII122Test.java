package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class BestTimeToBuyAnsSellStockII122Test extends TestCase {
    private final String name;
    private final int want;
    private final int[] prices;

    public BestTimeToBuyAnsSellStockII122Test(String name, int[] prices, int want) {
        this.name = name;
        this.prices = prices;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"one", new int[]{1}, 0},
                new Object[]{"two - profit > 0", new int[]{1, 2}, 1},
                new Object[]{"two - profit < 0", new int[]{2, 1}, 0},
                new Object[]{"high low high", new int[]{3, 1, 6}, 5},
                new Object[]{"high low high high", new int[]{3, 1, 6, 10}, 9},
                new Object[]{"high low high low", new int[]{3, 1, 6, 10, 1}, 9},
                new Object[]{"low high low", new int[]{1, 6, 2}, 5},
                new Object[]{"low high high", new int[]{1, 6, 10}, 9},
                new Object[]{"multiple mountains", new int[]{1, 3, 2, 6, 1}, 6}
        );
    }

    @Test
    public void test() {
        BestTimeToBuyAndSellStockII122.Solution solution = new BestTimeToBuyAndSellStockII122.Solution();
        Map<String, Function<int[], Integer>> fns = Map.of(
                "solution::maxProfit", solution::maxProfit,
                "solution::maxProfitDp", solution::maxProfitDp,
                "solution::maxProfitDp2DArray", solution::maxProfitDp2DArray,
                "solution::maxProfitDp1DArray", solution::maxProfitDp1DArray,
                "solution::maxProfitDpTempVar", solution::maxProfitDpTempVar
        );
        fns.forEach((methodName, method) -> {
            System.out.printf("Calling method %s\n", methodName);
            int got = method.apply(prices);
            assertEquals(want, got);
        });
    }
}
