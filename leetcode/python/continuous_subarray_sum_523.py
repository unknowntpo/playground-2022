from collections import defaultdict
from typing import List
import pytest

"""
LeetCode 523: Continuous Subarray Sum

Given an integer array nums and an integer k, return true if nums has a good subarray or false otherwise.

A good subarray is a subarray where:
- its length is at least 2, and
- the sum of the elements of the subarray is a multiple of k.

Note that:
- A subarray is a contiguous part of the array.
- An integer x is a multiple of k if there exists an integer n such that x = n * k. 0 is always a multiple of k.

Example 1:
Input: nums = [23,2,4,6,7], k = 6
Output: true
Explanation: [2, 4] is a continuous subarray of size 2 whose elements sum up to 6.

Example 2:
Input: nums = [23,2,6,4,7], k = 6
Output: true
Explanation: [23, 2, 6, 4, 7] is an continuous subarray of size 5 whose elements sum up to 42.
42 is a multiple of 6 because 42 = 7 * 6 and 7 is an integer.

Example 3:
Input: nums = [23,2,6,4,7], k = 13
Output: false

1 <= nums.length <= 105
0 <= nums[i] <= 109
0 <= sum(nums[i]) <= 231 - 1
1 <= k <= 231 - 1
"""


class Solution:
    def checkSubarraySum(self, nums: List[int], k: int) -> bool:
        # TODO: Implement this method
        n = len(nums)
        # lookup interval [i, j]
        for i in range(0, n):
            total = 0
            for j in range(i, n):
                total += nums[j]
                if (total / k).is_integer() and (j - i + 1) >= 2:
                    return True
        # == 1 or >= 1 ?
        return False

    # prefix sum
    def checkSubarraySum2(self, nums: List[int], k: int) -> bool:
        n = len(nums)
        ps = [0] * (n + 1)
        for i in range(0, n):
            ps[i + 1] = ps[i] + nums[i]
        # lookup interval [i, j]
        # [1, 3, 4],
        # [1, 1, 3, 12]
        for i in range(0, n):
            # total = 1
            for j in range(i, n):
                # total *= nums[j]
                total = ps[j + 1] - ps[i]
                if (total / k).is_integer() and (j - i + 1) >= 2:
                    return True
        # == 1 or >= 1 ?
        return False

    # prefix sum + hashmap (TLE) (store indices of l in cnt)
    def checkSubarraySum3(self, nums: List[int], k: int) -> bool:
        ans = 0
        n = len(nums)
        ps = [0] * (n + 1)
        for i in range(0, n):
            ps[i + 1] = ps[i] + nums[i]

        cnt = defaultdict(list)
        cnt[0] = [0]
        for r in range(0, n):
            # total = 1
            # total *= nums[j]
            """
            ( pj - pi ) % k == 0
            pj % k - pi % k == 0
            pj % k = pi % k
            """
            key = (ps[r + 1] % k + k) % k

            # total = ps[j + 1] - ps[i]
            if key in cnt:
                # for (j - i + 1) >= 2:
                for l in cnt[key]:
                    if r - l + 1 >= 2:
                        ans += 1
            cnt[key].append(r + 1)

        return ans > 0

    # prefix sum + hashmap (PASS)
    def checkSubarraySum4(self, nums: List[int], k: int) -> bool:
        has = False
        n = len(nums)
        ps = [0] * (n + 1)
        for i in range(0, n):
            ps[i + 1] = ps[i] + nums[i]

        cnt = defaultdict(int)
        cnt[0] = 0
        for r in range(0, n):
            # total = 1
            # total *= nums[j]
            """
            ( pj - pi ) % k == 0
            pj % k - pi % k == 0
            pj % k = pi % k
            """
            key = (ps[r + 1] % k + k) % k

            # total = ps[j + 1] - ps[i]
            if key in cnt:
                # for (j - i + 1) >= 2:
                if r - cnt[key] + 1 >= 2:
                    return True
            else:
                cnt[key] = r + 1

        return has


testCases = [
    {"name": "example 1", "nums": [23, 2, 4, 6, 7], "k": 6, "want": True},
    {"name": "example 2", "nums": [23, 2, 6, 4, 7], "k": 6, "want": True},
    {"name": "example 3", "nums": [23, 2, 6, 4, 7], "k": 13, "want": False},
    {"name": "length is 1, false", "nums": [1], "k": 4, "want": False},
    {"name": "length > 1, True", "nums": [2, 4, 4, 6], "k": 8, "want": True},
    {"name": "length > 1, False", "nums": [2, 4, 6], "k": 17, "want": False},
    {
        "name": "length > 1, matching subarray length is length",
        "nums": [2, 4, 6],
        "k": 12,
        "want": True,
    },
    {
        "name": "leetcode failed testcase",
        "nums": [23, 2, 4, 6, 6],
        "k": 7,
        "want": True,
    },
    {
        "name": "leetcode failed testcase2",
        "nums": [1, 0],
        "k": 2,
        "want": False,
    },
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
        for method_name in [
            "checkSubarraySum",
            "checkSubarraySum2",
            "checkSubarraySum3",
            "checkSubarraySum4",
        ]
    ],
    ids=id_func,
)
def test_check_subarray_sum(testCase, method_name):
    nums: List[int] = testCase["nums"]
    k: int = testCase["k"]
    want: bool = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums, k)
    assert got == want
