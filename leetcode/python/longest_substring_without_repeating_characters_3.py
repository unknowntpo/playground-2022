import pytest
from typing import List

"""
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
    def lengthOfLongestSubstring_official(self, s: str) -> int:
        return 0

    def lengthOfLongestSubstring(self, s: str) -> int:
        """
        {"name": "len(s) non-repeating chars", "s": "dvdf", "want": 3},
        {"name": "len(s) non-repeating chars: 7", "s": "abcdefg", "want": 7},
        {"name": "len(s) non-repeating chars: 7", "s": "abcckabd", "want": 5},
        {"name": "non-containing chars should be deleted from map", "s": "pwwkew", "want": 3},
        """
        maxLength = 0
        n = len(s)
        if n == 0:
            return 0

        l = 0
        r = 0

        m = {}
        # dvdf
        while l <= r and r < n:
            c = s[r]
            if c in m:
                # dedup
                #   l
                #       r
                # kbacdea
                # s[l] == s[r]
                while l < r and s[l] != s[r]:
                    m.pop(s[l])
                    l += 1
                # # skip same char
                # s[l] == s[r] or l == r
                l += 1
                r += 1
            else:
                m[c] = 1
                maxLength = max(maxLength, len(m))
                r += 1
        return maxLength

        # # s = "a"
        # # s = abkabcde
        # maxLength = 0
        # m = {}
        # for i in range(0, len(s)):
        #     # i = 0
        #     # i = 0
        #     if s[i] in m:
        #         if s[i] == s[i-k+1]:

        #         else:
        #             maxLength = max(len(m), maxLength)
        #             length = 0
        #             m = {s[i]: 1}
        #     else:
        #         m[s[i]] = 1
        #         maxLength = max(len(m), maxLength)
        # return maxLength


# 0 <= s.length <= 5 * 104
# s 由英文字母、数字、符号和空格组成

"""
s
0 <= len(s) <= 5 * 104
ele in s: [a_zA_Z|0_9|\s|char]

assume non-repeat length is k
so: 0 <= k <= len(s)

- forgot to update maxLength to max(maxLength, len(m))
- forgot to consider pop element from queue
"""

testCases = [
    {"name": "length is 0", "s": "", "want": 0},
    {"name": "only 1 element", "s": "a", "want": 1},
    {"name": "2 non-repeating chars", "s": "abkkk", "want": 3},
    {"name": "skipped repeating characters", "s": "dvdf", "want": 3},
    {"name": "len(s) non-repeating chars: 7", "s": "abcdefg", "want": 7},
    {"name": "continuous repeating chars", "s": "abcckabd", "want": 5},
    {"name": "continuous repeating chars", "s": "pwwkew", "want": 3},
]


# @pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
# def test_longest_substring_without_repeating_chars(testCase):
#     s: List[int] = testCase["s"]
#     want: int = testCase["want"]

#     sol = Solution()
#     method_names = ["lengthOfLongestSubstring", "lengthOfLongestSubstring_official"]

#     for method in method_names:
#         fn = getattr(sol)
#         got = fn(s)
#         assert got == want


# @pytest.mark.parametrize(
#     "testCase,method_name",
#     [
#         (testCase, method_name)
#         for testCase in testCases
#         for method_name in [
#             "lengthOfLongestSubstring",
#             "lengthOfLongestSubstring_official",
#         ]
#     ],
#     ids=lambda param: f"{param[0]['name']}_{param[1]}",
# )


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
            "lengthOfLongestSubstring_official",
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
