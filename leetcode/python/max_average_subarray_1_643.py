from math import floor
import pytest
from typing import List

"""
给给你一个由 n 个元素组成的整数数组 nums 和一个整数 k 。

请你找出平均数最大且 长度为 k 的连续子数组，并输出该最大平均数。

任何误差小于 10-5 的答案都将被视为正确答案。

 

示例 1：

输入：nums = [1,12,-5,-6,50,3], k = 4
输出：12.75
解释：最大平均数 (12-5-6+50)/4 = 51/4 = 12.75
示例 2：

输入：nums = [5], k = 1
输出：5.00000
 

提示：

n == nums.length
1 <= k <= n <= 105
-104 <= nums[i] <= 104
"""


class Solution:
    def findMaxAverage(self, nums: List[int], k: int) -> float:
        n = len(nums)
        maxAvg = -float("inf")
        avg = 0
        total = 0
        for i, c in enumerate(nums):
            total += c
            if i < k - 1:
                continue
            # FIXME: 0.00000
            # avg = floor((total / (10**5)) / k) * (10**5)
            avg = total / k

            maxAvg = max(maxAvg, avg)

            total -= nums[i - k + 1]
        return maxAvg


# 1 <= s.length <= 10^5
# s 由小写英文字母组成
# 1 <= k <= s.length
testCases = [
    {"name": "only 1 element - negative", "nums": [-1], "k": 1, "want": -1.00000},
    {"name": "only 1 element - divisible", "nums": [5], "k": 1, "want": 5.00000},
    {
        "name": "multi-elements, non divisible",
        "nums": [-45, 13, 2, 5, 6, 9],
        "k": 3,
        "want": 6.66666,
    },
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_max_num_of_vovels(testCase):
    nums: List[int] = testCase["nums"]
    k: int = testCase["k"]
    want: int = testCase["want"]

    sol = Solution()
    got = sol.findMaxAverage(nums, k)
    assert print("f{got:.5f}") == print(want)
