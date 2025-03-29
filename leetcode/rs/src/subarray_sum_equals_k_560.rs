use std::collections::HashMap;
use rstest::rstest;

struct Solution;

impl Solution {
    ///
    /// 给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的子数组的个数 。
    ///
    /// 子数组是数组中元素的连续非空序列。
    ///
    ///
    ///
    /// 示例 1：
    ///
    /// 输入：nums = [1,1,1], k = 2
    /// 输出：2
    /// 示例 2：
    ///
    /// 输入：nums = [1,2,3], k = 3
    /// 输出：2
    ///
    ///
    /// 提示：
    ///
    /// 1 <= nums.length <= 2 * 104
    /// -1000 <= nums[i] <= 1000
    /// -107 <= k <= 107
    pub fn subarray_sum(nums: Vec<i32>, k: i32) -> i32 {
        // enumerate:
        // n = len(nums)
        // from [i, n), how many matching subsets do we have ?
        // TC: O(n^2): SC: 1
        // [1, 1, 1], k = 2
        let mut res = 0;
        let mut acc = 0;
        let n = nums.len();
        for i in 0..n {
            acc = 0;
            for j in i..n {
                acc += nums[j];
                if acc == k {
                    res += 1;
                }
            }
        }

        res
    }

    pub fn subarray_sum2(nums: Vec<i32>, k: i32) -> i32 {
        // [1, 3, 4]
        // [0, 1, 4, 8]
        // [i, j] = ps[j+1] - ps[i]
        // [1, 2] = ps[3] - ps[1] = 8 - 1 = 7
        // [0, 2] = ps[3] - ps[0] = 8 - 0 = 8
        let n = nums.len();
        if n == 0 {
            return 0;
        }

        let mut ps: Vec<i32> = vec![0; n + 1];
        for i in 0..n {
            ps[i + 1] = ps[i] + nums[i];
        }

        let mut res = 0;
        let mut acc = 0;
        for i in 0..n {
            for j in i..n {
                // sum in [i, j]
                acc = ps[j + 1] - ps[i];
                if acc == k {
                    res += 1;
                }
            }
        }

        res
    }

    // TC: O(n), SC: O(n)
    pub fn subarray_sum3(nums: Vec<i32>, k: i32) -> i32 {
        // [1, 3, 4]
        // [0, 1, 4, 8]
        // [i, j] = ps[j+1] - ps[i]
        // [1, 2] = ps[3] - ps[1] = 8 - 1 = 7
        // [0, 2] = ps[3] - ps[0] = 8 - 0 = 8
        // m: {0: 1}
        // m: {0: 1, 1: 1}
        // m: {0: 1, 1: 1, 4: 1}
        // m: {0: 1, 1: 1, 4: 1, 8: 1}
        /// nums [1,-1,0], k = 0
        /// ps [0, 1, 0, 0]
        /// cnt: {0: 1}, cnt: {0: 1, 1: 1}
        ///
        let n = nums.len();
        if n == 0 {
            return 0;
        }

        let mut ps: Vec<i32> = vec![0; n + 1];
        for i in 0..n {
            ps[i + 1] = ps[i] + nums[i];
        }

        // for j, we wanna find [i, j] where sum == k
        // ps[j+1] - ps[i] = k
        // we can record ps[i] in hashMap every time
        // m {}

        let mut res = 0;
        let mut acc = 0;
        // map from sum to num of ps eq to sum
        // [1, 1, 2], k = 3
        // [0, 1, 2, 4], ps
        // i = 0, cnt: {0}

        let mut cnt: HashMap<i32, i32> = HashMap::new();
        // FIXME: Do we need this ?
        cnt.insert(0, 1);
        for j in 0..n {
            // sum in [i, j]
            // j = 0, 1, 2
            // cnt: {0: 1}
            // cnt: {0: 1, 1: 1}
            // cnt: {0: 1, 1: 1, 2: 1}
            let x = ps[j + 1];
            // x: 1, 2, 4
            // let diff = x - k;
            // 4- 3 = 1
            let matched_cnt = cnt.get(&(x - k)).unwrap_or(&0).clone();
            // matched_cnt: 0, 0, 1
            res += matched_cnt;
            // res = 0, 0, 1
            cnt.insert(x, cnt.get(&x).unwrap_or(&0) + 1);
            // cnt: {0: 1, 1: 1}
            // cnt: {0: 1, 1: 1, 2: 1}
            // cnt: {0: 1, 1: 1, 2: 1, 4: 1}
       }
        // for i in 0..n {
        //     for j in i..n {
        //         // sum in [i, j]
        //         acc = ps[j + 1] - ps[i];
        //         if acc == k {
        //             res += 1;
        //         }
        //     }
        // }

        res
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    // type TestFn = fn(Vec<i32>, i32) -> i32;

    #[rstest]
    #[case::example_1(vec![1, 1, 1], 2, 2)]
    #[case::example_2(vec![1, 2, 3], 3, 2)]
    #[case::self_example_1(vec![1], 2, 0)]
    #[case::self_example_2(vec![1, 3, 5], 9, 1)]
    #[case::self_example_3_test_negative_number(vec![1, 3, -1, 5], 3, 2)]
    #[case::self_example_4_multi_match(vec![1, -1, 0], 0, 3)]
    fn test_subsets(#[case] nums: Vec<i32>, #[case] k: i32, #[case] want: i32) {
        let fns = vec![Solution::subarray_sum, Solution::subarray_sum2, Solution::subarray_sum3];

        for f in fns {
            // COPY v.s. CLONE
            let got = f(nums.clone(), k);
            assert_eq!(got, want);
        }
    }
}
