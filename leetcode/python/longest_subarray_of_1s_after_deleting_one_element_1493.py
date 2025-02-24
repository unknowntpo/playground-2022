from collections import Counter
import pytest
from typing import Dict, List

"""
给你一个二进制数组 nums ，你需要从中删掉一个元素。

请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。

如果不存在这样的子数组，请返回 0 。

 

提示 1：

输入：nums = [1,1,0,1]
输出：3
解释：删掉位置 2 的数后，[1,1,1] 包含 3 个 1 。
示例 2：

输入：nums = [0,1,1,1,0,1,1,0,1]
输出：5
解释：删掉位置 4 的数字后，[0,1,1,1,1,1,0,1] 的最长全 1 子数组为 [1,1,1,1,1] 。
示例 3：

输入：nums = [1,1,1]
输出：2
解释：你必须要删除一个元素。
 

提示：

1 <= nums.length <= 105
nums[i] 要么是 0 要么是 1 。

可參考 leetcode 1004 0x3f 題解
Ref: https://leetcode.cn/problems/max-consecutive-ones-iii/solutions/2126631/hua-dong-chuang-kou-yi-ge-shi-pin-jiang-yowmi
"""


class Solution:
    def longestSubarray(self, nums: List[int]) -> int:
        k = 1
        l = cnt0 = ans = 0
        for r, x in enumerate(nums):
            cnt0 += 1 - x  # count number of 0
            while cnt0 > k:
                cnt0 -= 1 - nums[l]
                l += 1
            ans = max(ans, r - l)
        return ans

    def longestSubarray_backup(self, nums: List[int]) -> int:
        # matching
        # [l, r]: matched array with only 1 element needs to be deleted
        # l: left bound: must be 1
        # .  l
        # 0, 1, 1, 1,
        # r - 1: right bound: must be 1
        # for [l, r], update maxLength = max(maxLength, r - l - 1)

        # [0, 1, 1, 1, 0, 1, 1, 0, 1]
        l = 0
        while nums[l] != 1:
            l += 1
        maxLength = 0
        numOfZero = 0
        print("Hello")
        for r, x in enumerate(nums[l:]):
            if x == 1:
                maxLength = max(maxLength, r - l + 1 - 1)
                continue
            if x == 0 and numOfZero == 0 and r < len(nums) - 1 and nums[r + 1] == 1:
                # meet first 0, can keep expanding
                numOfZero = 1
                continue
            # x == 0, numOfZero == 1
            # numOfZero is 1
            while nums[l] != 0:
                l += 1
            # now, nums[l] == 0
            numOfZero = 0
            print(l)
            l += 1
            maxLength = max(maxLength, r - l - 1)

        return maxLength


testCases = [
    {"name": "only 1 element, match", "nums": [1], "want": 0},
    {"name": "only 2 element, match", "nums": [1, 1], "want": 1},
    {"name": "only 1 element, no match", "nums": [0], "want": 0},
    {"name": "only 2 element, match", "nums": [0, 1], "want": 1},
    {"name": "matching multi times", "nums": [0, 1, 1, 0, 1], "want": 3},
    {"name": "matching one time, all element is 1", "nums": [1, 1, 1, 1, 1], "want": 4},
    {
        "name": "leetcode failed test",
        "nums": [0, 1, 1, 1, 0, 1, 1, 0, 1],
        "want": 5,
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
        for method_name in ["longestSubarray"]
    ],
    ids=id_func,
)
def test_1493(testCase, method_name):
    nums: List[int] = testCase["nums"]
    want: int = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums)
    assert got == want
