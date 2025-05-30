package com.leetcode;


import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * 给你一个非负整数数组 nums 和一个整数 target 。
 * <p>
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
 * <p>
 * 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [1,1,1,1,1], target = 3
 * 输出：5
 * 解释：一共有 5 种方法让最终目标和为 3 。
 * -1 + 1 + 1 + 1 + 1 = 3
 * +1 - 1 + 1 + 1 + 1 = 3
 * +1 + 1 - 1 + 1 + 1 = 3
 * +1 + 1 + 1 - 1 + 1 = 3
 * +1 + 1 + 1 + 1 - 1 = 3
 * 示例 2：
 * <p>
 * 输入：nums = [1], target = 1
 * 输出：1
 * <a href="https://leetcode.cn/problems/target-sum/">...</a>
 */
public class TargetSum494 {
    static class Solution {
        public int findTargetSumWaysOld(int[] nums, int target) {
            // dfs(i, j): ways to compute result == j from [0,i]
            // dfs(i, j) = dfs(i-1, j-nums[i]) + dfs(i-1, j+nums[i])
            // dfs(-1, 0) = 1
            // dfs(-1, others) = 0
            // [1], 1
            // examples
            // dfs(0, 1) = dfs(-1, 0) + dfs(-1, 2)
            BiFunction<Integer, Integer, Integer>[] dfs = new BiFunction[1];
            dfs[0] = (i, j) -> {
                if (i < 0) {
                    return j == 0 ? 1 : 0;
                }
                return dfs[0].apply(i - 1, j - nums[i]) + dfs[0].apply(i - 1, j + nums[i]);
            };

            return dfs[0].apply(nums.length - 1, target);
        }

        public int findTargetSumWays(int[] nums, int target) {
            // p: sum of all numbers with + sign
            // q: sum of all numbers with - sign
            // s: sum(nums)
            // p + q = s
            // p - q = target
            // p = (s + target) / 2
            // q = (s - target) / 2
            // if target < 0,
            // q = (s - |target|) / 2
            // s >= target, (s - |target|) / 2 % 2 == 0

            int S = Arrays.stream(nums).sum();
            int s = S - Math.abs(target);
            if (s < 0 || s % 2 != 0) {
                return 0;
            }

            int m = s / 2;

            BiFunction<Integer, Integer, Integer>[] dfs = new BiFunction[1];
            dfs[0] = (i, j) -> {
                if (i < 0) {
                    return j == 0 ? 1 : 0;
                }
                return dfs[0].apply(i - 1, j - nums[i]) + dfs[0].apply(i - 1, j);
            };

            return dfs[0].apply(nums.length - 1, m);
        }

        public int findTargetSumWays2DArray(int[] nums, int target) {
            int S = Arrays.stream(nums).sum();
            int s = S - Math.abs(target);
            if (s < 0 || s % 2 != 0) {
                return 0;
            }

            int m = s / 2;

            int n = nums.length;
            int[][] dfs = new int[n + 1][m+1];
            dfs[0][0] = 1;
            // dfs(i, j) = dfs(i-1, j - nums[i]) + dfs(i-1, j)
            // dfs[i][j] = (j >= nums[i]) ? dfs[i-1][j-nums[i]] : 0) + dfs[i-1][j], i from [-1, n-1]
            // dfs[i+1][j] = (j >= nums[i]) ? dfs[i][j-nums[i]] : 0) + dfs[i][j], i from [0, n-1]

            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= m; j++) {
                    dfs[i+1][j] = ((j >= nums[i]) ? dfs[i][j-nums[i]] : 0) + dfs[i][j];
                }
            }

            return dfs[n][m];
        }

        public int findTargetSumWays1DArray(int[] nums, int target) {
            int S = Arrays.stream(nums).sum();
            int s = S - Math.abs(target);
            if (s < 0 || s % 2 != 0) {
                return 0;
            }

            int m = s / 2;

            int[] dfs = new int[m+1];
            dfs[0] = 1;
            // dfs(i, j) = dfs(i-1, j - nums[i]) + dfs(i-1, j)
            // dfs[i][j] = (j >= nums[i]) ? dfs[i-1][j-nums[i]] : 0) + dfs[i-1][j], i from [-1, n-1]
            // dfs[i+1][j] = (j >= nums[i]) ? dfs[i][j-nums[i]] : 0) + dfs[i][j], i from [0, n-1]

            for (int num : nums) {
                for (int j = m; j >= 0; j--) {
                    dfs[j] = ((j >= num) ? dfs[j - num] : 0) + dfs[j];
                }
            }

            return dfs[m];
        }
    }
}
