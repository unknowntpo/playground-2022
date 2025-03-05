from typing import List
import pytest
from collections import defaultdict

"""
LeetCode 974: Subarray Sums Divisible by K

Given an integer array nums and an integer k, return the number of non-empty subarrays that have a sum divisible by k.

A subarray is a contiguous part of an array.

Example 1:
Input: nums = [4,5,0,-2,-3,1], k = 5
Output: 7
Explanation: There are 7 subarrays with a sum divisible by k = 5:
[4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]

Example 2:
Input: nums = [5], k = 9
Output: 0
"""


class Solution:
    def subarraysDivByK(self, nums: List[int], k: int) -> int:
        # [4, 5, 0, -2, -3, 1], k = 5
        n = len(nums)
        s = [0] * (n + 1)
        ans = 0
        for i in range(n):
            s[i + 1] = s[i] + nums[i]

        # [4, 5, 0, -2, -3, 1], k = 5
        # s
        # [0,4,9,9,7,4,5]

        # find interval in [i, r]
        for r in range(n):
            # r = 1
            # r =
            for l in range(0, r + 1):
                # l = 0
                # total = s[2] - s[0] = 9 - 0 = 9
                # l = 1
                # total = s[2] - s[1] = 5
                # (sr - sl) % k == 0
                # sr % k == sl % k
                # key: si %k
                total = s[r + 1] - s[l]
                if total % k == 0:
                    ans += 1
                    # ans = 1
        return ans


testCases = [
    {"name": "example 1", "nums": [4, 5, 0, -2, -3, 1], "k": 5, "want": 7},
    {"name": "example 2", "nums": [5], "k": 9, "want": 0},
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
        for method_name in ["subarraysDivByK"]
    ],
    ids=id_func,
)
def test_subarrays_div_by_k(testCase, method_name):
    nums: List[int] = testCase["nums"]
    k: int = testCase["k"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums, k)
    assert got == want
