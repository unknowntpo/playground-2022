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
    def lengthOfLongestSubstring(self, s: str) -> int:
        # s = "a"
        # s = abkabcde
        maxLength = 0
        m = {}
        for i in range(0, len(s)):
            # i = 0
            # i = 0
            if s[i] in m:
                if s[i] == s[i-k+1]:
                    
                else:
                    maxLength = max(len(m), maxLength)
                    length = 0
                    m = {s[i]: 1}
            else:
                m[s[i]] = 1
                maxLength = max(len(m), maxLength)
        return maxLength


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
    {"name": "only 1 element - is vowel", "s": "a", "want": 1},
    {"name": "2 non-repeating chars", "s": "abkkk", "want": 3},
    {"name": "len(s) non-repeating chars", "s": "dvdf", "want": 3},
    {"name": "len(s) non-repeating chars", "s": "abcdefg", "want": 7},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_longest_substring_without_repeating_chars(testCase):
    s: List[int] = testCase["s"]
    want: int = testCase["want"]

    sol = Solution()
    got = sol.lengthOfLongestSubstring(s)
    assert got == want
