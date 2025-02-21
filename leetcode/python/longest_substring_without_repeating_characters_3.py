import pytest
from typing import List

"""
link: https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/

给定一个字符串 s ，请你找出其中不含有重复字符的 最长 
子串
 的长度。

 

示例 1:

输入: s = "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
示例 2:

输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
示例 3:

输入: s = "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 

提示：

0 <= s.length <= 5 * 104
s 由英文字母、数字、符号和空格组成
"""


class Solution:
    """ """

    def lengthOfLongestSubstring(self, s: str) -> int:
        n = len(s)
        l = 0
        maxLength = 0
        m = {}
        for r, x in enumerate(s):
            if x not in m:
                m[x] = 1
                maxLength = max(maxLength, r - l + 1)
                continue
            while s[l] != x:
                m.pop(s[l])
                l += 1
            l += 1

        return maxLength


testCases = [
    {"name": "length is 0", "s": "", "want": 0},
    {"name": "only 1 element", "s": "a", "want": 1},
    {"name": "2 non-repeating chars", "s": "abkkk", "want": 3},
    {"name": "skipped repeating characters", "s": "dvdf", "want": 3},
    {"name": "len(s) non-repeating chars: 7", "s": "abcdefg", "want": 7},
    {"name": "continuous repeating chars", "s": "abcckabd", "want": 5},
    {"name": "continuous repeating chars", "s": "pwwkew", "want": 3},
]


def id_func(param):
    if isinstance(param, dict):
        return param["name"]
    return param


@pytest.mark.parametrize(
    "testCase,method_name",
    [
        (testCase, method_name)
        for testCase in testCases
        for method_name in [
            "lengthOfLongestSubstring",
        ]
    ],
    ids=id_func,
)
def test_longest_substring_without_repeating_chars(testCase, method_name):
    s: List[int] = testCase["s"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(s)
    assert got == want
