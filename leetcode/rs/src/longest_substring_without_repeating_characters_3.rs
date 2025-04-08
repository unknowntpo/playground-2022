use std::{
    collections::{HashMap, HashSet},
    iter::Enumerate,
};

struct Solution {}

impl Solution {
    /// 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
    ///
    /// 示例 1:
    /// 输入: s = "abcabcbb"
    /// 输出: 3
    /// 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
    ///
    /// 示例 2:
    /// 输入: s = "bbbbb"
    /// 输出: 1
    /// 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
    ///
    /// 示例 3:
    /// 输入: s = "pwwkew"
    /// 输出: 3
    /// 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
    ///      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
    ///
    /// 提示：
    /// 0 <= s.length <= 5 * 10^4
    /// s 由英文字母、数字、符号和空格组成
    pub fn length_of_longest_substring(s: String) -> i32 {
        // "dvdf"
        // #[case("pwwkew", 3)]
        // pwwkew
        let n = s.len();
        let s = s.as_bytes();
        let mut max_length = 0;
        for l in 0..n {
            let mut cnt: HashMap<u8, bool> = HashMap::new();
            for r in l..n {
                if cnt.get(&s[r]).is_none() {
                    max_length = max_length.max(r - l + 1);
                    cnt.insert(s[r], true);
                } else {
                    break;
                }
            }
        }

        max_length as i32
    }

    pub fn length_of_longest_substring2(s: String) -> i32 {
        // "dvdf"
        // pwwkew
        let n = s.len();
        let s = s.as_bytes();
        let mut max_length = 0;
        let mut cnt: HashMap<u8, bool> = HashMap::new();

        let mut l = 0;
        // l = 0
        for (r, x) in s.iter().enumerate() {
            // r = 0, 1, 2, 3, 4, 5
            // l = 0, 2
            if cnt.get(x).is_none() {
                max_length = max_length.max(r - l + 1);
                // max_length = 2, 3
                cnt.insert(*x, true);
                // cnt: {p, w}
                // cnt: {w, k}
                // cnt: {w, k, e}
                continue;
            }
            // has dup
            // while cnt.get(&s[l]).is_some() {
            while s[l] != s[r] {
                cnt.remove(&s[l]);
                // cnt: {w}
                l += 1;
                // l = 1
            }
            // s[l] == s[r]
            l += 1;
            // l = 2, 3
        }
        // for l in 0..n {
        //     let mut cnt: HashMap<u8, bool> = HashMap::new();
        //     for r in l..n {
        //         if cnt.get(&s[r]).is_none() {
        //             max_length = max_length.max(r - l + 1);
        //             cnt.insert(s[r], true);
        //         } else {
        //             break;
        //         }
        //     }
        // }

        max_length as i32
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use rstest::rstest;

    #[rstest]
    #[case("abcabcbb", 3)]
    #[case("bbbbb", 1)]
    #[case("pwwkew", 3)]
    #[case("", 0)]
    #[case(" ", 1)]
    #[case("au", 2)]
    #[case("dvdf", 3)]
    #[case("@ab", 3)]
    fn test_length_of_longest_substring(#[case] s: &str, #[case] expected: i32) {
        assert_eq!(
            expected,
            Solution::length_of_longest_substring(s.to_string())
        );
        assert_eq!(
            expected,
            Solution::length_of_longest_substring2(s.to_string())
        );
    }
}
