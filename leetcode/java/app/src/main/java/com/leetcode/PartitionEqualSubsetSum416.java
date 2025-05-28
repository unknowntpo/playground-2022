package com.leetcode;


import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [1,5,11,5]
 * 输出：true
 * 解释：数组可以分割成 [1, 5, 5] 和 [11] 。
 * 示例 2：
 * <p>
 * 输入：nums = [1,2,3,5]
 * 输出：false
 * 解释：数组不能分割成两个元素和相等的子集。
 * <p>
 * 提示：
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 100
 */
public class PartitionEqualSubsetSum416 {
    static class Solution {
        public boolean canPartition(int[] nums) {
            int sum = Arrays.stream(nums).sum();
            if (sum % 2 != 0) {
                return false;
            }
            sum = sum / 2;

            // dfs(i, target): from [0, i], num of ways to pick numbers so that sum of them == target
            // dfs(i, target) = dfs(i - 1, target - nums[i]) + dfs(i-1, target)
            // dfs(-1) = 0

            BiFunction<Integer, Integer, Integer>[] dfs = new BiFunction[1];
            dfs[0] = (i, target) -> {
                if (i < 0) {
                    return 0;
                }
                if (target == 0) {
                    return 1;
                }

                return dfs[0].apply(i - 1, target - nums[i]) + dfs[0].apply(i-1, target);
            };

            return dfs[0].apply(nums.length - 1, sum) > 0;
       }

        public boolean canPartition2DArray(int[] nums) {
            int sum = Arrays.stream(nums).sum();
            if (sum % 2 != 0) {
                return false;
            }
            sum = sum / 2;

            // dfs(i, target): from [0, i], num of ways to pick numbers so that sum of them == target
            // dfs(i, target) = dfs(i - 1, target - nums[i]) + dfs(i-1, target)
            // dfs(-1) = 0
            int n = nums.length;
            int[][] dfs = new int[n + 1][sum + 1];

            for (int i = 1; i <= n; i++) {
                dfs[i][sum]
            }

            BiFunction<Integer, Integer, Integer>[] dfs = new BiFunction[1];
            dfs[0] = (i, target) -> {
                if (i < 0) {
                    return 0;
                }
                if (target == 0) {
                    return 1;
                }

                return dfs[0].apply(i - 1, target - nums[i]) + dfs[0].apply(i-1, target);
            };

            return dfs[0].apply(nums.length - 1, sum) > 0;
        }
    }
}
