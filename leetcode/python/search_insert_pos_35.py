from collections import deque
from ctypes.wintypes import tagMSG
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree

"""
给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。

请必须使用时间复杂度为 O(log n) 的算法。

 

示例 1:

输入: nums = [1,3,5,6], target = 5
输出: 2
示例 2:

输入: nums = [1,3,5,6], target = 2
输出: 1
示例 3:

输入: nums = [1,3,5,6], target = 7
输出: 4
 

提示:

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums 为 无重复元素 的 升序 排列数组
-104 <= target <= 104
"""


class Solution:
    def searchInsert(self, nums: List[int], target: int) -> int:
        """

        find lower_bound which lower <= target
        """

        # [l, r]
        # in interval: elements that needs to be verified
        # invariant
        # for i > r, nums[i] >= target
        # for i < l, nums[i] < target
        #  l
        #        r
        #    m
        # [1,3,5,6]
        l = 0
        r = len(nums) - 1
        while l <= r:
            mid = l + (r - l) // 2
            if target > nums[mid]:
                l = mid + 1
            else:
                r = mid - 1
        # return l
        return r + 1


testCases = [
    {"name": "head", "nums": [1,3,5,6], "target": -1, "want": 0},
    {"name": "middle", "nums": [1,3,5,6], "target": 5, "want": 2},
    {"name": "middle - 2", "nums": [1,3,5,6], "target": 2, "want": 1},
    {"name": "tail", "nums": [1,3,5,6], "target": 7, "want": 4},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_search_insert_pos(testCase):
    nums = testCase["nums"]
    target = testCase["target"]
    sol = Solution()

    method_names = ['searchInsert']
    fns = [getattr(sol, method_name) for method_name in method_names]
    for f in fns:
        print(f"testing {f.__name__}...")
        res = f(nums, target)
        assert res == testCase["want"]
