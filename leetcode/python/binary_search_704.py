from collections import deque
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree

"""
给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。




提示：

你可以假设 nums 中的所有元素是不重复的。
n 将在 [1, 10000]之间。
nums 的每个元素都将在 [-9999, 9999]之间。

Ref: https://leetcode.cn/problems/binary-search/

Example:

[1,3,4,7,8], target: 4, res: 2
[1], target: 1, res: 0
[3], target: 1, res: -1

"""
class Solution:
    # [left, right] (closed interval)
    def search(self, nums: List[int], target: int) -> int:
        l = -1
        r = len(nums)
        while l + 1 < r:
            # FIXME: may overflow
            # m = int(l / 2 + r / 2)
            # FIXME why equals
           m = l + (r - l) // 2
           midNum = nums[m]
           if midNum == target:
               return m
           if target > midNum:
               l = m
           else:
                r = m

        return -1

    def search_slow(self, nums: List[int], target: int) -> int:
        for i, num in enumerate(nums):
            if num == target:
                return i
        return -1

testCases = [
    {"name": "found", "nums": [-1, 0, 3, 5, 9, 12], "target": 9, "want": 4},
    {"name": "not found", "nums": [-1, 0, 3, 5, 9, 12], "target": 2, "want": -1},
    {"name": "only 1 number - found", "nums": [1], "target": 1, "want": 0},
    {"name": "only 1 number - not found", "nums": [3], "target": 1, "want": -1},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_binary_search(testCase):
    nums = testCase["nums"]
    target = testCase["target"]
    sol = Solution()
    res = sol.search(nums, target)
    assert res == testCase["want"]
