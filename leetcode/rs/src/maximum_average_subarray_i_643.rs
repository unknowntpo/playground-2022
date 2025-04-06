struct Solution {}

impl Solution {
    /// 给你一个由 n 个元素组成的整数数组 nums 和一个整数 k 。
    /// 请你找出平均数最大且 长度为 k 的连续子数组，并输出该最大平均数。
    /// 任何误差小于 10^-5 的答案都将被视为正确答案。
    ///
    /// 示例 1：
    /// 输入：nums = [1,12,-5,-6,50,3], k = 4
    /// 输出：12.75
    /// 解释：最大平均数 (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75
    ///
    /// 示例 2：
    /// 输入：nums = [5], k = 1
    /// 输出：5.00000
    ///
    /// 提示：
    /// n == nums.length
    /// 1 <= k <= n <= 10^5
    /// -10^4 <= nums[i] <= 10^4
    pub fn find_max_average(nums: Vec<i32>, k: i32) -> f64 {
        // TODO: Implement the solution
        // 5, 3 , 2, 1, 11
        // 5] [3 , 2, 1], 11
        // 5, 3, 2, 1, 11
        // [      ]
        let n = nums.len();
        let mut max_s = i32::MIN;
        let mut acc = 0;
        // #[case(vec![5], 1, 5.0)]
        // #[case::custom(vec![5,3,2,1,11], 2, 6.0)]
        // n = 1
        for i in 0..n {
            // i: 0
            // consume new number
            // k = 2
            acc += nums[i];
            // acc = 5, 8

            // max_avg = 5 / 2 = 2.5
            // max_avg = 8 / 2 = 4
            // let k: usize = k.try_into().unwrap();
            if i + 1 < k as usize {
                continue;
            }
            // length of window == k
            // #[case(vec![1,12,-5,-6,50,3], 4, 12.75)]

            max_s = max_s.max(acc);

            acc -= nums[i + 1 - k as usize];
        }

        max_s as f64 / k as f64
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use rstest::rstest;

    #[rstest]
    #[case(vec![1,12,-5,-6,50,3], 4, 12.75)]
    #[case(vec![5], 1, 5.0)]
    #[case::custom(vec![5,3,2,1,11], 2, 6.0)]
    fn test_find_max_average(#[case] nums: Vec<i32>, #[case] k: i32, #[case] expected: f64) {
        let result = Solution::find_max_average(nums, k);
        assert!((result - expected).abs() < 1e-5);
    }
}
