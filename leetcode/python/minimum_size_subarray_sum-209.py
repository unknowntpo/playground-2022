import pytest
from typing import List

"""
给定一个含有 n 个正整数的数组和一个正整数 target 。

找出该数组中满足其总和大于等于 target 的长度最小的 
子数组
 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。

 

示例 1：

输入：target = 7, nums = [2,3,1,2,4,3]
输出：2
解释：子数组 [4,3] 是该条件下的长度最小的子数组。
示例 2：

输入：target = 4, nums = [1,4,4]
输出：1
示例 3：

输入：target = 11, nums = [1,1,1,1,1,1,1,1]
输出：0
 

提示：

1 <= target <= 109
1 <= nums.length <= 105
1 <= nums[i] <= 104
 

进阶：

如果你已经实现 O(n) 时间复杂度的解法, 请尝试设计一个 O(n log(n)) 时间复杂度的解法。
"""


class Solution:
    # misunderstood ans and s meaning
    def minSubArrayLen(self, target: int, nums: List[int]) -> int:
        n = len(nums)  # 5
        s = 0  # 0
        l = 0  # 0
        ans = n + 1  # 6
        # nums: [1, 2, 4, 2, 6], target: 3
        for r, x in enumerate(nums):
            # r = 0, x = 1
            # r = 1, x = 2
            # r = 2, x = 4
            s += x
            # s = 1, 3, 7
            while s >= target:
                ans = min(
                    ans, r - l + 1
                )  # ans = min(6, 1 - 0 + 1) = 2, # ans = min(2, 2 - 1 + 1) = 2
                s -= nums[l]  # 0, 3 - 1 = 2,
                l += 1  # l = 1

        return ans if ans < n + 1 else 0


testCases = [
    {
        "name": "length is 1, match",
        "target": 1,
        "nums": [1],
        "want": 1,  # [1]
    },
    {
        "name": "length is 1, no match",
        "target": 3,
        "nums": [1],
        "want": 0,
    },
    {
        "name": "single match, sum is equals to target",
        "target": 6,
        "nums": [1, 2, 4, 1, 1],
        "want": 2,  # [2, 4]
    },
    {
        "name": "multiple match, sum is equals to target",
        "target": 3,
        "nums": [1, 2, 4, 2, 1],
        "want": 1,  # [1,2] or [2,1]
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
        for method_name in ["minSubArrayLen"]
    ],
    ids=id_func,
)
def test_longest_substring_without_repeating_chars(testCase, method_name):
    target: int = testCase["target"]
    nums: List[int] = testCase["nums"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(target, nums)
    assert got == want
