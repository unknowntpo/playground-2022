use std::cmp::max;

struct Solution {}

impl Solution {
    /// 给你字符串 s 和整数 k 。
    /// 请返回字符串 s 中长度为 k 的单个子字符串中可能包含的最大元音字母数。
    /// 英文中的 元音字母 为（a, e, i, o, u）。
    ///
    /// 示例 1：
    /// 输入：s = "abciiidef", k = 3
    /// 输出：3
    /// 解释：子字符串 "iii" 包含 3 个元音字母。
    ///
    /// 示例 2：
    /// 输入：s = "aeiou", k = 2
    /// 输出：2
    /// 解释：任意长度为 2 的子字符串都包含 2 个元音字母。
    ///
    /// 示例 3：
    /// 输入：s = "leetcode", k = 3
    /// 输出：2
    /// 解释："lee"、"eet" 和 "ode" 都包含 2 个元音字母。
    ///
    /// 示例 4：
    /// 输入：s = "rhythms", k = 4
    /// 输出：0
    /// 解释：字符串 s 中不含任何元音字母。
    ///
    /// 示例 5：
    /// 输入：s = "tryhard", k = 4
    /// 输出：1
    ///
    /// 提示：
    /// 1 <= s.length <= 10^5
    /// s 由小写英文字母组成
    /// 1 <= k <= s.length
    pub fn max_vowels(s: String, k: i32) -> i32 {
        // #[case::two_match("abcdef", 2, 3)] // ab, de, ef
        // #[case("abciiidef", 3, 3)]
        // s: abcdef, k = 2
        // find from [i, j] where match the criteria, and add to res;
        let mut res = 0;
        let n = s.len();
        for i in 0..n {
            let mut cnt = 0;
            for j in i..n {
                // cnt: num of vowels in [i, j]
                let c = s.chars().nth(j).unwrap();
                match c {
                    'a' | 'e' | 'i' | 'o' | 'u' => {
                        cnt += 1;
                    }
                    _ => {}
                }
                // get len k substring and has vowels
                if (j - i + 1) as i32 == k {
                    res = max(res, cnt);
                    break;
                }
            }
        }

        res
    }

    pub fn max_vowels2(s: String, k: i32) -> i32 {
        let s = s.as_bytes();
        let k = k as usize;
        let mut ans = 0;
        let mut vowel = 0;
        for (i, &c) in s.iter().enumerate() {
            // 1. 进入窗口
            if c == b'a' || c == b'e' || c == b'i' || c == b'o' || c == b'u' {
                vowel += 1;
            }
            if i < k - 1 {
                // 窗口大小不足 k
                continue;
            }
            // 2. 更新答案
            ans = ans.max(vowel);
            // 3. 离开窗口
            let out = s[i + 1 - k];
            if out == b'a' || out == b'e' || out == b'i' || out == b'o' || out == b'u' {
                vowel -= 1;
            }
        }
        ans
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use rstest::rstest;

    #[rstest]
    #[case("abciiidef", 3, 3)]
    #[case::no_matching_k_greater_then_s_length("k", 3, 0)]
    #[case::no_matching_k_less_then_s_length("kbdfg", 3, 0)]
    #[case::three_matching_substrings("abcdef", 2, 1)] // ab, de, ef
    #[case::three_matching_substrings_maxlen_is_2("abcdeaf", 2, 2)] // ab, de, ea
    fn test_max_vowels(#[case] s: &str, #[case] k: i32, #[case] expected: i32) {
        assert_eq!(Solution::max_vowels(s.to_string(), k), expected);
        assert_eq!(Solution::max_vowels2(s.to_string(), k), expected);
    }
}
