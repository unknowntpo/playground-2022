from collections import deque
from ctypes.wintypes import tagMSG
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree

"""
给你一个字符数组 letters，该数组按非递减顺序排序，以及一个字符 target。letters 里至少有两个不同的字符。

返回 letters 中大于 target 的最小的字符。如果不存在这样的字符，则返回 letters 的第一个字符。

 

示例 1：

输入: letters = ["c", "f", "j"]，target = "a"
输出: "c"
解释：letters 中字典上比 'a' 大的最小字符是 'c'。
示例 2:

输入: letters = ["c","f","j"], target = "c"
输出: "f"
解释：letters 中字典顺序上大于 'c' 的最小字符是 'f'。
示例 3:

输入: letters = ["x","x","y","y"], target = "z"
输出: "x"
解释：letters 中没有一个字符在字典上大于 'z'，所以我们返回 letters[0]。
 

提示：

2 <= letters.length <= 104
letters[i] 是一个小写字母
letters 按非递减顺序排序
letters 最少包含两个不同的字母
target 是一个小写字母
"""


class Solution:
    def nextGreatestLetter(self, letters: List[str], target: str) -> str:
        def lower_bound(letters: List[str], _target: str) -> int:
            """
            l
                               r

                      m
              [c,f,k, z, z, z]
            invariant:
            letters[<=l] < target
            letters[>=r] >= target


            so, we need to return letters[r]
            """
            l = -1
            r = len(letters)
            # l == 0, r = 1
            while l + 1 < r:
                mid = l + (r - l) // 2
                """
                target > letters[mid], set l = mid
                target < letters[mid], set r = mid
                target = letters[mid], set r = mid
                """
                if _target > letters[mid]:
                    l = mid
                else:
                    r = mid
            return r
            # return l + 1

        next_char = chr(ord(target) + 1)
        next_idx = lower_bound(letters, next_char)
        if next_idx == len(letters):
            return letters[0]
        return letters[next_idx]


testCases = [
    {"name": "found: case0", "letters": ["c", "f", "k"], "target": "a", "want": "c"},
    {"name": "found: case1", "letters": ["c", "f", "k"], "target": "c", "want": "f"},
    {"name": "not found", "letters": ["x", "x", "y", "y"], "target": "z", "want": "x"},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_next_greatest_letter(testCase):
    letters = testCase["letters"]
    target = testCase["target"]
    sol = Solution()

    method_names = ['nextGreatestLetter']
    fns = [getattr(sol, method_name) for method_name in method_names]
    for f in fns:
        print(f"testing {f.__name__}...")
        res = f(letters, target)
        assert res == testCase["want"]
