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

    # [left,right)
    # e.g. [0, 1)
    # [-1, 0, 3, 5, 9, 12], target: 9, want: 4
    def search_left_close_right_open(self, nums: List[int], target: int) -> int:
        l = 0
        r = len(nums)
        while l < r:
            m = l + (r - l) // 2
            mVal = nums[m]
            if mVal == target:
                return m
            if mVal < target:
                l = m + 1
            else:
                r = m
        return -1

    # (left,right]
    # e.g. (0, 1]
    # [-1, 0, 3, 5, 9, 12], target: 9, want: 4
    # [0], target: 3
    # [3], target: 1
    # [-1, 0, 3, 5, 9, 12], target: 2, want: -1
    # [1], target: 1
    """
    this will failed, 
    testcase
    # [1], target: 1
    becausea m will be out-of-bound (-1 + 0) // 2 = -1
    which is invalid.
    breaks invariant
    l < 0 <= m <= right < len(nums) - 1 
    """
    def search_left_open_right_close(self, nums: List[int], target: int) -> int:
        l = -1
        r = len(nums) - 1
        # l = -1, r = 5
        # l = -1, r = 0
        while l < r:
            m = l + (r - l) // 2
            # m = 4 / 2 = 2
            # m = -1 + 0 // 2 = -1
            # m = (1 + (-1)) // 2 = 0
            # m = 0
            # l = -1, r = 0, m = -1, out of bound
            if m < 0 or m > len(nums) - 1:
                return -1
            mVal = nums[m]
            # mVal = 3
            # mVal = -1
            if mVal == target:
                return m
            if mVal < target:
                l = m + 1
            else:
                r = m
                # r = 2 - 1 = 1, l = -1
                # r = 0 - 1 = -1, l = -1
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


    method_names = ['search', 'search_slow', 'search_left_close_right_open']
    fns = [getattr(sol, method_name) for method_name in method_names]
    for f in fns:
        print(f"testing {f.__name__}...")
        res = f(nums, target)
        assert res == testCase["want"]
