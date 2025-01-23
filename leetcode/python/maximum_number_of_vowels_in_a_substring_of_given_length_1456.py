import pytest
from typing import List

"""
给你字符串 s 和整数 k 。

请返回字符串 s 中长度为 k 的单个子字符串中可能包含的最大元音字母数。

英文中的 元音字母 为（a, e, i, o, u）。

 

示例 1：

输入：s = "abciiidef", k = 3
输出：3
解释：子字符串 "iii" 包含 3 个元音字母。
示例 2：

输入：s = "aeiou", k = 2
输出：2
解释：任意长度为 2 的子字符串都包含 2 个元音字母。
示例 3：

输入：s = "leetcode", k = 3
输出：2
解释："lee"、"eet" 和 "ode" 都包含 2 个元音字母。
示例 4：

输入：s = "rhythms", k = 4
输出：0
解释：字符串 s 中不含任何元音字母。
示例 5：

输入：s = "tryhard", k = 4
输出：1
 

提示：

1 <= s.length <= 10^5
s 由小写英文字母组成
1 <= k <= s.length
"""


class Solution:
    """
    O(nk), n = len(s)
    """

    def maxVowelsSlow(self, s: str, k: int) -> int:
        # 0 <= i < n - k + 1
        n = len(s)
        maxVowelNums = 0
        for i in range(0, n - k + 1):
            vowelNums = 0
            for j in range(i, i + k):
                if s[j] in ["a", "e", "i", "o", "u"]:
                    vowelNums += 1
            maxVowelNums = max(vowelNums, maxVowelNums)
        return maxVowelNums

    def isVowel(self, c: str) -> bool:
        return c in ["a", "e", "i", "o", "u"]

    def maxVowelsFast(self, s: str, k: int) -> int:
        n = len(s)
        maxVowelNums = 0

        # initial vowel nums
        vowelNums = sum(1 for i in range(k) if self.isVowel(s[i]))
        maxVowelNums = vowelNums

        for i in range(k, n):
            vowelNums += int(self.isVowel(s[i])) - int(self.isVowel(s[i - k]))
            maxVowelNums = max(vowelNums, maxVowelNums)

        return maxVowelNums

    def maxVowels(self, s: str, k: int) -> int:
        # return self.maxVowelsSlow(s, k)
        return self.maxVowelsFast(s, k)


# 1 <= s.length <= 10^5
# s 由小写英文字母组成
# 1 <= k <= s.length
testCases = [
    {"name": "only 1 element - is vowel", "s": "a", "k": 1, "want": 1},
    {"name": "only 1 element - is vowel", "s": "x", "k": 1, "want": 0},
    {"name": "k == len(s)", "s": "leetcode", "k": 8, "want": 4},
    {"name": "case1", "s": "abciiidef", "k": 3, "want": 3},
    {"name": "case2", "s": "aeiou", "k": 2, "want": 2},
    {"name": "case3", "s": "leetcode", "k": 3, "want": 2},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_max_num_of_vovels(testCase):
    s: List[int] = testCase["s"]
    k: int = testCase["k"]
    want: int = testCase["want"]

    sol = Solution()
    got = sol.maxVowels(s, k)
    assert got == want
