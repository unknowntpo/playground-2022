package com.leetcode;

import com.leetcode.BinaryTree.TreeNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;


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
public class BestTimeToBuyAndSellStock121 {
    static class Solution {
        public int maxProfit(int[] prices) {
            // FIXME: java assert
            // { 2, 7, 3, 1, 10, 7 } , want: 9
            assert prices.length > 0;

            int buyInPrice = prices[0];
            int maxProfit = Integer.MIN_VALUE;
            for (int i = 1; i < prices.length; i++) {
                int currentPrice = prices[i];
                maxProfit = Math.max(maxProfit, currentPrice - buyInPrice);
                if (currentPrice - buyInPrice < 0) {
                    // buyIn
                    buyInPrice = currentPrice;
                }
            }

            return Math.max(maxProfit, 0);


            // Def of maxProfit:
            // 1. maxProfit = sellOutPrice - buyInPrice
            // 2. maxProfit > 0
            // buyIn price should be lowest.
            // sell price should be highest and sellOutDay > buyInDay
            // maxProfit > 0
            // when to update buyInPrice ?
            //   when we find a buyInPrice2 that buyInPrice2 < buyInPrice
            // when to update sellOutPrice ?
            //   when we find a buyInPrice2 that buyInPrice2 < buyInPrice
        }
    }
}
