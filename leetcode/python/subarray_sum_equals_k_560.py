from typing import List
import pytest
from collections import defaultdict

"""
LeetCode 560: Subarray Sum Equals K

Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:
Input: nums = [1,1,1], k = 2
Output: 2

Example 2:
Input: nums = [1,2,3], k = 3
Output: 2
"""


class Solution:
    def subarraySumOn2(self, nums: List[int], k: int) -> int:
        ans = 0
        n = len(nums)
        # [-1, -1, 1], k = 0
        for i in range(n):
            total = 0
            # find matching subarray in [0, j]
            for j in range(i, n):
                total += nums[j]
                if total == k:
                    ans += 1
        return ans

    def subarraySumOn2PrefixSum(self, nums: List[int], k: int) -> int:
        """
        nums: [1, -1, 0], k = 0
        ps: [0, 1, 0, 0]

        nums: [1, 2, 3], k = 3, 'want': 2
        ps: [0, 1, 3, 5]


        """
        ans = 0
        n = len(nums)
        ps = [0] * (n + 1)
        for i in range(0, n):
            ps[i + 1] = ps[i] + nums[i]

        for r in range(0, len(ps)):
            for l in range(0, r):
                if ps[r] - ps[l] == k:
                    print(f"ps[{r}] - ps[{l}] == {k}")
                    ans += 1

        return ans

    def subarraySum(self, nums: List[int], k: int) -> int:
        """
        [1,1,1], k=2

        s:
        [0, 1, 2, 3]
        """
        s = [0] * (len(nums) + 1)
        for i, x in enumerate(nums):
            s[i + 1] = s[i] + x

        ans = 0
        cnt = defaultdict(int)
        # cnt = {}
        """
        [1,2,1], cnt: 

        ps
        [0, 1, 3, 4]

        cnt:
        {0: 1}
        {0: 1, 1: 1}
        {0: 1, 1: 1, 3: 1, 4: 1}

        [0, 1, -1, 1]
        ps:
        [0, 0, 1, 0, 1]

        cnt:
        {0: 1} 0: ps0
        {0: 1+1, } ps0, ps1
        {0: 1+1, 1:1}, 0: ps0, ps1, 1: ps2
        {0: 1+1, 1:1+1} 0: ps0, ps1, 1: ps2, ps3
        {0: 1+1, 1:1+1, 2: 1} 0: ps0, ps1, 1: ps2, ps3, 2: ps4

        k = 0

        cnt: [0, i+1) prefix sum 
        """
        for sj in s:
            # sj: s[0], s[1] = 1, s[2] = 2, s[3] = 3
            # si = sj -k
            # ans += si
            ans += cnt[sj - k]
            # ans = 0 + cnt[s[0] - 2] = 0 + cnt[-2] = 0
            # ans = 0 + cnt[1 - 2] = 0 + cnt[-1] = 0
            # ans = 1 + cnt[s[2] - 2] = 1 + cnt[0] = 1
            # ans = 1 + cnt[s[3] - 2] = 1 + cnt[1] = 2 + 1 = 3
            print(f"ans += cnt[{sj - k}] = {cnt[sj-k]}")
            cnt[sj] += 1
            print(f"cnt[{sj}] = {cnt[sj]}")

            # cnt[s0] = cnt[0] + 1 = 1
            # cnt[s1=1] = 0 + 1 = 1, cnt: {0: 0, 1: 1}
            # cnt[s2=2] = cnt[2] + 1, cnt: {0: 0, 1: 1, 2: 1}
        return ans


testCases = [
    {"name": "example 1", "nums": [1, 1, 1], "k": 2, "want": 2},
    {"name": "example 2", "nums": [1, 2, 3], "k": 3, "want": 2},
    {"name": "example 3", "nums": [-1, -1, 1], "k": 0, "want": 1},
    {"name": "example 4", "nums": [1, -1, 0], "k": 0, "want": 3},
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
        for method_name in ["subarraySum", "subarraySumOn2", "subarraySumOn2PrefixSum"]
    ],
    ids=id_func,
)
def test_subarray_sum(testCase, method_name):
    nums: List[int] = testCase["nums"]
    k: int = testCase["k"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums, k)
    assert got == want
