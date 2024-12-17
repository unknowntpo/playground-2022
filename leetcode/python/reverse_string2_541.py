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
        # s = abcde, k = 3
        # {"name": "case1", "s": "abcd", "k": 2, "want": "bacd"},
        # s = cbadgbbecka, k = 3
        # s = abcd
        out = list(s)
        # for 2k range
        for i in range(0, len(s), 2 * k):
            # i = 0
            # i = 2 * 3 = 6
            # len(out) == 11
            # 11 % (2* 3) >= 3
            # 11 % 6 == 5 >= 3
            # 4 % (4) >= k:
            # if len(out) % (2 * k) >= k:
                # 5 % (2 * 3) = 5 % 6 = 5 >= 3
            l = i
            # l = 0
            # l = 6
            r = i + k - 1
            # r = 0 + 3 - 1 = 2
            # r = 6 + 3 - 1 = 8
            while r < len(out) and l < r:
                # 0 < 2
                # 6 < 8
                # out: {[cba][dgb]}{[bec][ka]}
                #                    l
                #                      r
                # out: {[abc][dgb]}{[bec][ka]}
                out[l], out[r] = out[r], out[l]
                # out: {[abc][dgb]}{[bec][ka]}
                # out: {[abc][dgb]}{[ceb][ka]}
                l+=1
                # l = 1
                # l = 6+1 = 7
                r-=1
                # r = 1
                # r = 8 - 1 = 7
        # # for k range
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
