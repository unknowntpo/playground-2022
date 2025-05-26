package com.leetcode;


/**
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * <p>
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * <p>
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * 提示：
 *
 * 1 <= prices.length <= 105
 * 0 <= prices[i] <= 104
 * <a href="https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/description/">...</a>
 */
public class BestTimeToBuyAndSellStockII122 {
    static class Solution {
        public int maxProfit(int[] prices) {
            int profit = 0;
            for (int i = 1 ; i < prices.length; i++) {
                if (prices[i] > prices[i-1]) {
                    profit += prices[i] - prices[i-1];
                }
            }
            return profit;
        }
    }
}
