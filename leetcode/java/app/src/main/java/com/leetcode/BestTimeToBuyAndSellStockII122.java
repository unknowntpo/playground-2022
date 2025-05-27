package com.leetcode;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * <p>
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * <p>
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * 提示：
 * <p>
 * 1 <= prices.length <= 105
 * 0 <= prices[i] <= 104
 * <a href="https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/description/">...</a>
 */
public class BestTimeToBuyAndSellStockII122 {
    static class Solution {
        public int maxProfit(int[] prices) {
            int profit = 0;
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > prices[i - 1]) {
                    profit += prices[i] - prices[i - 1];
                }
            }
            return profit;
        }

        public int maxProfitDp(int[] prices) {
            BiFunction<Integer, Integer, Integer>[] fns = new BiFunction[1];
            fns[0] = (i, holdStock) -> {
                if (i < 0) {
                    return holdStock != 0 ? Integer.MIN_VALUE : 0;
                }
                if (holdStock != 0) {
                    return Math.max(fns[0].apply(i - 1, 0) - prices[i], fns[0].apply(i - 1, 1));
                }
                return Math.max(fns[0].apply(i - 1, 0), fns[0].apply(i - 1, 1) + prices[i]);
            };
            return fns[0].apply(prices.length - 1, 0);
        }

        public int maxProfitDp2DArray(int[] prices) {
            // dfs(i, 0) = arr[i+1, 0]
            // dfs(i, 1) = arr[i+1, 1]
            // dfs(0, 0) = 0,
            // dfs(0, 1) = -inf

            // [1]
            // n = 1
            int n = prices.length;
            int[][] dfs = new int[n + 1][2];

            System.out.print("size of dfs: " + dfs.length);
            System.out.print("size of dfs[0]: " + dfs[0].length);

            dfs[0][0] = 0;
            dfs[0][1] = Integer.MIN_VALUE;

            // [0, -inf]
            // [0, -1]

            for (int i = 1; i <= n; i++) {
                dfs[i][0] = Math.max(dfs[i - 1][0], dfs[i - 1][1] + prices[i - 1]);
                dfs[i][1] = Math.max(dfs[i - 1][0] - prices[i - 1], dfs[i - 1][1]);
            }

            return dfs[n][0];
        }

        public int maxProfitDp1DArray(int[] prices) {
            int n = prices.length;
            int[] dfs = new int[2];
            dfs[0] = 0;
            dfs[1] = Integer.MIN_VALUE;
            for (int i = 1; i <= n; i++) {
                int newF0 = Math.max(dfs[0], dfs[1] + prices[i - 1]);
                dfs[1] = Math.max(newF0 - prices[i - 1], dfs[1]);
                dfs[0] = newF0;
            }

            return dfs[0];
        }

        public int maxProfitDpTempVar(int[] prices) {
            int n = prices.length;
            int f0 = 0;
            int f1 = Integer.MIN_VALUE;
            for (int i = 1; i <= n; i++) {
                int newF0 = Math.max(f0, f1 + prices[i - 1]);
                f1 = Math.max(newF0 - prices[i - 1], f1);
                f0 = newF0;
            }

            return f0;
        }
    }
}
