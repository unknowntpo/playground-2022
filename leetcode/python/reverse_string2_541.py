import pytest
from typing import List


"""
给定一个字符串 s 和一个整数 k，从字符串开头算起，每计数至 2k 个字符，就反转这 2k 字符中的前 k 个字符。

    如果剩余字符少于 k 个，则将剩余字符全部反转。
    如果剩余字符小于 2k 但大于或等于 k 个，则反转前 k 个字符，其余字符保持原样。

Ref: https://leetcode.cn/problems/reverse-string-ii/description/
"""

"""
len(s): l

m := l % 2k:
- 0 < m < k:
    reverse all words
    e.g. 
        s = a, k = 2
            res = [a]
- k <= m < 2k:
    reverse first k words
    e.g. 
    s = cbadg, k = 3
    group: [[cba][dg]]
    res = {[abc][dg]}
    s = cbadgbbecka, k = 3
    group: {[cba][dgb]}{[bec][ka]}
    res = {[abc][bgd]}{[ceb][ka]}
"""
class Solution:
    def reverseStr(self, s: str, k: int) -> str:
        out = list(s)
        for i in range(0, len(s), 2 * k):
            l = i
            r = i + k -1 if i + k - 1 < len(out) - 1 else len(out) - 1
            while l < r:
                out[l], out[r] = out[r], out[l]
                l+=1
                r-=1
        return ''.join(out)


# 输入：s = "abcdefg", k = 2
# 输出："bacdfeg"
#
# 示例 2：
#
# 输入：s = "abcd", k = 2
# 输出："bacd"
#
#
testCases = [
    {"name": "case0", "s": "abcdefg", "k": 2, "want": "bacdfeg"},
    {"name": "case1", "s": "abcd", "k": 2, "want": "bacd"},
    {"name": "case2", "s": "cbadgbbecka", "k": 3, "want": "abcdgbcebka"},
    {"name": "k too big", "s": "cb", "k": 3, "want": "bc"},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_reverseString2(testCase):
    sol = Solution()
    s = testCase["s"]
    k = testCase["k"]
    want = testCase["want"]
    got = sol.reverseStr(s,k)
    assert got == want
