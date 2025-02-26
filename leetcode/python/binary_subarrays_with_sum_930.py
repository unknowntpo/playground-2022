from typing import List
import pytest

"""
Ref: https://leetcode.cn/problems/binary-subarrays-with-sum/description/

LeetCode 930: Binary Subarrays With Sum

Given a binary array nums and an integer goal, return the number of non-empty subarrays
with a sum equal to goal.

A subarray is a contiguous part of the array.

Example 1:
Input: nums = [1,0,1,0,1], goal = 2
Output: 4
Explanation: The 4 subarrays are:
- [1,0,1] from index 0 to 2
- [1,0,1] from index 1 to 3
- [0,1,0,1] from index 0 to 3
- [1,0,1] from index 2 to 4

Example 2:
Input: nums = [0,0,0,0,0], goal = 0
Output: 15
"""


class Solution:
    def numSubarraysWithSum(self, nums: List[int], goal: int) -> int:
        def atMost(target: int) -> int:
            ans = l = total = 0
            for r, x in enumerate(nums):
                total += x
                while l <= r and total > target:
                    total -= nums[l]
                    l += 1
                ans += r - l + 1
            return ans

        a = atMost(goal)
        b = atMost(goal - 1)
        print(a, b)

        return atMost(goal) - atMost(goal - 1)

    def numSubarraysWithSum2(self, nums: List[int], goal: int) -> int:
        def atLeast(k):
            count = 0
            left = 0
            current_sum = 0
            for right in range(len(nums)):
                current_sum += nums[right]
                # 移动左指针直到当前窗口的和小于k
                while left <= right and current_sum >= k:
                    current_sum -= nums[left]
                    left += 1
                # 累加满足条件的左端点数目
                count += left
            return count

        upper = atLeast(goal)
        lower = atLeast(goal + 1)

        return upper - lower


testCases = [
    {"name": "example 1", "nums": [1, 0, 1, 0, 1], "goal": 2, "want": 4},
    {"name": "example 2", "nums": [0, 0, 0, 0, 0], "goal": 0, "want": 15},
    {"name": "example 3", "nums": [1, 0, 1, 0, 1], "goal": 3, "want": 1},
    {"name": "example 1", "nums": [1, 1, 1, 1, 1], "goal": 0, "want": 0},
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
        for method_name in ["numSubarraysWithSum", "numSubarraysWithSum2"]
    ],
    ids=id_func,
)
def test_num_subarrays_with_sum(testCase, method_name):
    nums: List[int] = testCase["nums"]
    goal: int = testCase["goal"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums, goal)
    assert got == want
