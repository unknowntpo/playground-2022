import pytest
from typing import List

"""
给你一个只包含字符 'a'，'b' 和 'c' 的字符串 s ，你可以执行下面这个操作（5 个步骤）任意次：

选择字符串 s 一个 非空 的前缀，这个前缀的所有字符都相同。
选择字符串 s 一个 非空 的后缀，这个后缀的所有字符都相同。
前缀和后缀在字符串中任意位置都不能有交集。
前缀和后缀包含的所有字符都要相同。
同时删除前缀和后缀。
请你返回对字符串 s 执行上面操作任意次以后（可能 0 次），能得到的 最短长度 。

 

示例 1：

输入：s = "ca"
输出：2
解释：你没法删除任何一个字符，所以字符串长度仍然保持不变。
示例 2：

输入：s = "cabaabac"
输出：0
解释：最优操作序列为：
- 选择前缀 "c" 和后缀 "c" 并删除它们，得到 s = "abaaba" 。
- 选择前缀 "a" 和后缀 "a" 并删除它们，得到 s = "baab" 。
- 选择前缀 "b" 和后缀 "b" 并删除它们，得到 s = "aa" 。
- 选择前缀 "a" 和后缀 "a" 并删除它们，得到 s = "" 。
示例 3：

输入：s = "aabccabba"
输出：3
解释：最优操作序列为：
- 选择前缀 "aa" 和后缀 "a" 并删除它们，得到 s = "bccabb" 。
- 选择前缀 "b" 和后缀 "bb" 并删除它们，得到 s = "cca" 。
 

提示：

1 <= s.length <= 105
s 只包含字符 'a'，'b' 和 'c' 。
"""


class Solution:
    """
    {"name": "length is 0", "s": "", "want": 0},
    {
        "name": "prefix and suffix has different length, and can be trimmed",
        "s": "aabcbbaaa",
        "want": 1,
    },
    """

    def minimumLength(self, s: str) -> int:
        """
        l is on first pos of prefix that no need to be trimmed, l = 0
        l is on last pos of suffix that no need to be trimmed, l = len(s) - 1

        loop
            check if first and last char are the same, if not, break
            advance l and r until s[l] != s[l-1], s[r] != s[r + 1], note that 0 <= l < r <= len(s) - 1

        aabcbbaaa

        can not distinguish between only 1 ele, or no ele
        """
        l = 0
        r = len(s) - 1

        while l < r and s[l] == s[r]:
            l += 1
            r -= 1
            while l <= r and s[l] == s[l - 1]:
                l += 1
            # FIXME: what is the meaning: l == r ?
            while l <= r and s[r] == s[r + 1]:
                r -= 1
            # FIXME: what is the meaning: l == r ?
        return r - l + 1
        # return 0 if r == l else r - l + 1


testCases = [
    {"name": "length is 1, no char can be trimmed", "s": "a", "want": 1},
    {"name": "length is 2, no char can be trimmed", "s": "ab", "want": 2},
    {"name": "length is 2, chars can be trimmed", "s": "aa", "want": 0},
    {
        "name": "prefix and suffix has different length, and can be trimmed",
        "s": "aabcbbaaa",
        "want": 1,
    },
    {"name": "length > 5, and can not be trimmed", "s": "abcbcb", "want": 6},
    {
        "name": "length > 5, odd, and all chars can be trimmed",
        "s": "abbbbbbbbba",
        "want": 0,
    },
    {
        "name": "length > 5, even num, and all chars can be trimmed",
        "s": "abbbbbbbbbbbbbbbbbbba",
        "want": 0,
    },
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
        for method_name in ["minimumLength"]
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
