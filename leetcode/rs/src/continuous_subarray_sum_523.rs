use std::cmp::min;
use rstest::rstest;
use std::collections::HashMap;

struct Solution;

impl Solution {
    /// 给你一个整数数组 nums 和一个整数 k ，如果 nums 有一个 好的子数组 返回 true ，否则返回 false：
    ///
    /// 一个 好的子数组 是：
    ///
    /// 长度 至少为 2 ，且
    /// 子数组元素总和为 k 的倍数。
    /// 注意：
    ///
    /// 子数组 是数组中 连续 的部分。
    /// 如果存在一个整数 n ，令整数 x 符合 x = n * k ，则称 x 是 k 的一个倍数。0 始终 视为 k 的一个倍数。
    ///
    ///
    /// 示例 1：
    ///
    /// 输入：nums = [23,2,4,6,7], k = 6
    /// 输出：true
    /// 解释：[2,4] 是一个大小为 2 的子数组，并且和为 6 。
    /// 示例 2：
    ///
    /// 输入：nums = [23,2,6,4,7], k = 6
    /// 输出：true
    /// 解释：[23, 2, 6, 4, 7] 是大小为 5 的子数组，并且和为 42 。
    /// 42 是 6 的倍数，因为 42 = 7 * 6 且 7 是一个整数。
    /// 示例 3：
    ///
    /// 输入：nums = [23,2,6,4,7], k = 13
    /// 输出：false
    ///
    ///
    /// 提示：
    ///
    /// 1 <= nums.length <= 105
    /// 0 <= nums[i] <= 109
    /// 0 <= sum(nums[i]) <= 231 - 1
    /// 1 <= k <= 231 - 1
    pub fn check_subarray_sum(nums: Vec<i32>, k: i32) -> bool {
        // #[case::example_4(vec![1], 3, false)]
        let n = nums.len();
        for i in 0..n {
            let mut acc = 0;
            for j in i..n {
                acc += nums[j];
                if acc % k == 0 && (j - i + 1) >= 2 {
                    return true;
                }
            }
        }

        false
    }
    // prefix[i+1] = prefix[i] + nums[i]

    pub fn check_subarray_sum2(nums: Vec<i32>, k: i32) -> bool {
        let n = nums.len();
        let mut cnt: HashMap<i32, i32> = HashMap::new();
        cnt.insert(0, 0);
        /// cnt: {0: 0}
        /// [1,3,4], k = 8
        let mut ps = 0;
        // ps is ps
        // #[case::example_2(vec![23,2,6,4,7], 6, true)]
        // ps: [0, 23, 25, 31, 35, 42]
        // ps: [0, 1, 4, 8]
        for i in 1..=n {
            // i = 1, 2, 3
            ps += nums[i - 1];
            // ps = 1, 4, 8
            // (psi - psj) % k == 0
            // psi % k = psj % k
            let key = ps % k;
            // key = 1 % 8 = 1, 4 % 8 = 4, 8 % 8 = 0
            let psj_op = cnt.get(&key);
            // 3 - 0 = 3
            if psj_op.is_some() && i as i32 - psj_op.unwrap() >= 2 {
                return true;
            }

            // no val: insert key: key: val: i
            // has val: insert key: key, val: min(cnt[key], i)
            let val = min(i as i32, *cnt.get(&key).unwrap_or(&(i as i32)));
            cnt.insert(key, val);
            // cnt: {0: 0, 1: 1, 4: 2, }
        }

        false
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[rstest]
    #[case::example_1(vec![23, 2, 4, 6, 2], 6, true)]
    #[case::example_2(vec![23,2,6,4,7], 6, true)]
    #[case::example_3(vec![23,2,6,4,7], 13, false)]
    #[case::example_len_not_enough(vec![1], 1, false)]
    #[case::example_len_enough_but_not_match(vec![1, 0], 2, false)]
    #[case::example_len_enough_but_not_match(vec![1,0,1,0,1], 4, false)]
    #[case::example_true(vec![1, 3, 4], 8, true)]
    #[case::example_7(vec![1, 3, 4, 7, 12], 14, true)]
    fn test_continuous_subarray_sum(#[case] nums: Vec<i32>, #[case] k: i32, #[case] want: bool) {
        let fns = vec![Solution::check_subarray_sum, Solution::check_subarray_sum2];

        for f in fns {
            // COPY v.s. CLONE
            let got = f(nums.clone(), k);
            assert_eq!(got, want);
        }
    }
}
