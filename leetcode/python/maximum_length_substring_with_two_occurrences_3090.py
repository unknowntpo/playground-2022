from collections import Counter
import pytest
from typing import Dict, List

"""
给你一个字符串 s ，请找出满足每个字符最多出现两次的最长子字符串，并返回该
子字符串
的 最大 长度。

 

示例 1：

输入： s = "bcbbbcba"

输出： 4

解释：

以下子字符串长度为 4，并且每个字符最多出现两次："bcbbbcba"。

示例 2：

输入： s = "aaaa"

输出： 2

解释：

以下子字符串长度为 2，并且每个字符最多出现两次："aaaa"。

 

提示：

2 <= s.length <= 100
s 仅由小写英文字母组成。
"""


class Solution:
    def maximumLengthSubstring(self, s: str) -> int:
        l = 0
        cnt = Counter()
        maxLength = 0
        for r, x in enumerate(s):
            cnt[x] += 1
            while cnt[x] > 2:
                cnt[s[l]] -= 1
                l += 1
            maxLength = max(maxLength, r - l + 1)
        return maxLength


testCases = [
    # 1 type of char, exist <= 2 times
    {"name": "1 type of char, exist <= 2 times", "s": "aa", "want": 2},
    # 1 type of char, exist > 2 times
    {"name": "1 type of char, exist > 2 times", "s": "aaa", "want": 2},
    # 2 type of char, exist <= 2 times
    {"name": "2 type of char, exist <= 2 times", "s": "abab", "want": 4},
    # 2 type of char, one exist > 2 times, ans should be 3
    {"name": "2 type of char, exist > 2 times", "s": "ababb", "want": 4},
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
        for method_name in ["maximumLengthSubstring"]
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
