from collections import deque
from ctypes.wintypes import tagMSG
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree

"""
给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。

如果数组中不存在目标值 target，返回 [-1, -1]。

你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。

示例 1：

输入：nums = [5,7,7,8,8,10], target = 8
输出：[3,4]
示例 2：

输入：nums = [5,7,7,8,8,10], target = 6
输出：[-1,-1]
示例 3：

输入：nums = [], target = 0
输出：[-1,-1]
 

提示：

0 <= nums.length <= 105
-109 <= nums[i] <= 109
nums 是一个非递减数组
-109 <= target <= 109
"""


class Solution:
    """
    输入：nums = [5,7,7,8,8,10], target = 8
    输出：[3,4]
    示例 2：
    """

    def searchRange(self, nums: List[int], target: int) -> List[int]:
        """

        lower_bound: find the lower_bound of index which nums[lower] >= nums[n]

        ""
        Example
        [5,7,7,8,8,10]

        invariant:
        - nums[>r] >= n
        - nums[<=l-1] < n
        """

        def lower_bound(nums: List[int], n: int):
            print(n)
            # invariant: [l,r] close interval
            # in interval: values needs to be search

            # [5,7,7,8,8,10]
            # target: 9
            l = 0
            r = len(nums) - 1
            # l = 0, r = 5
            while l <= r:
                mid = l + (r - l) // 2
                # mid = 0 + ( 5 - 0 ) // 2 = 2
                # mid = 3 + (5-3) // 2 = 4
                # mid = 5
                midVal = nums[mid]
                if n > midVal:
                    # 8 > 7
                    # 10 > 8
                    l = mid + 1
                    # blue: [mid+1, r]
                    # l = 2 + 1 = 3, r = 5
                    # l = 4 + 1 = 5, r = 5
                else:
                    r = mid - 1
            print(l)
            return l


        def lower_bound2(nums: List[int], n: int):
            """
                lower_bound: find the lower_bound of index which nums[lower] >= nums[n]
                ele in [l, r): not sure

            [l, r)
            invariant:
            - nums[<l] < n
            - nums[>=r] >= n
            ""
            Example
            [5,7,7,8,8,10]

                  r | b
            [5,7,7,8,8,10]
            """
            l = 0
            r = len(nums)
            while l < r:
                mid = l + (r - l) // 2
                if n > nums[mid]:
                    l = mid + 1 # n is at [mid+1, r)
                else:
                    r = mid
            return l

        def lower_bound3(nums: List[int], n: int):
            """
                lower_bound: find the lower_bound of index which nums[lower] >= nums[n]

            (l, r)
            invariant:
            - nums[0, l] < n
            - nums[>=r] >= n
            ""
            Example
            [5,7,7,8,8,10]

                  r | b
            [5,7,7,8,8,10]
            """
            l = -1
            r = len(nums)
            while l+1 < r:
                mid = l + (r - l) // 2
                if n > nums[mid]:
                    l = mid # n is at (mid, r)
                else:
                    r = mid # n is at (l, mid)
            return r

        lower = lower_bound3(nums, target)
        if lower == len(nums) or nums[lower] != target:
            return [-1, -1]

        # at here, target must in nums
        upper = lower_bound3(nums, target + 1) - 1

        return [lower, upper]


testCases = [
    {"name": "found", "nums": [5, 7, 7, 8, 8, 10], "target": 8, "want": [3, 4]},
    {"name": "not found", "nums": [5, 7, 7, 8, 8, 10], "target": 6, "want": [-1, -1]},
    {"name": "empty", "nums": [], "target": 0, "want": [-1, -1]},
    {"name": "only 1 number - found", "nums": [1], "target": 1, "want": [0, 0]},
    {"name": "only 1 number - not found", "nums": [1], "target": 3, "want": [-1, -1]},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_find_first_last_pos(testCase):
    nums = testCase["nums"]
    target = testCase["target"]
    sol = Solution()

    method_names = ['searchRange']
    fns = [getattr(sol, method_name) for method_name in method_names]
    for f in fns:
        print(f"testing {f.__name__}...")
        res = f(nums, target)
        assert res == testCase["want"]
