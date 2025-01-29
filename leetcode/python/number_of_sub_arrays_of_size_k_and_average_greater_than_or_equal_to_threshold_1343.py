import pytest
from typing import List

"""
给你一个整数数组 arr 和两个整数 k 和 threshold 。

请你返回长度为 k 且平均值大于等于 threshold 的子数组数目。

 

示例 1：

输入：arr = [2,2,2,2,5,5,5,8], k = 3, threshold = 4
输出：3
解释：子数组 [2,5,5],[5,5,5] 和 [5,5,8] 的平均值分别为 4，5 和 6 。其他长度为 3 的子数组的平均值都小于 4 （threshold 的值)。
示例 2：

输入：arr = [11,13,17,23,29,31,7,5,2,3], k = 3, threshold = 5
输出：6
解释：前 6 个长度为 3 的子数组平均值都大于 5 。注意平均值不是整数。
 

提示：

1 <= arr.length <= 105
1 <= arr[i] <= 104
1 <= k <= arr.length
0 <= threshold <= 104
给你字符串 s 和整数 k 。
"""


class Solution:
    """
    "arr": [11, 13, 17, 23, 29, 31, 7, 5, 2, 3],
    "k": 3,
    "threshold": 5,

    """

    def numOfSubarrays(self, arr: List[int], k: int, threshold: int) -> int:
        n = len(arr)
        s = 0
        total = 0
        for i in range(0, n):
            s += arr[i]

            if i < k - 1:
                continue

            if s / k >= threshold:
                total += 1

            s -= arr[i - k + 1]

        return total


"""
k: 

threshold: 10
arr

- k > len(arr)
    - must be 0
- k < len(arr)
    - no match threshold 
    - only one match threshold
    - multi match threshold
    - continuous match threshold




"""
testCases = [
    # {
    #     "name": "k > len(arr), so there's no match",
    #     "arr": [10],
    #     "k": 3,
    #     "threshold": 4,
    #     "want": 0,
    # },
    # {
    #     "name": "k < len(arr), no match threshold",
    #     "arr": [2, 2, 2, 3, 4, 5],
    #     "k": 3,
    #     "threshold": 30,
    #     "want": 0,
    # },
    # {
    #     "name": "k < len(arr), only one match threshold",
    #     "arr": [5],
    #     "k": 1,
    #     "threshold": 4,
    #     "want": 1,
    # },
    # {
    #     "name": "k < len(arr), multi match threshold",
    #     "arr": [2, 2, 5, 6, 10, 13, 8],
    #     "k": 2,
    #     "threshold": 3,
    #     # [2,5], [5,6], [6,10], [10,13], [13,8]
    #     "want": 5,
    # },
    # {
    #     "name": "k < len(arr), multi match threshold",
    #     "arr": [2, 2, 2, 2, 5, 5, 5, 8],
    #     "k": 3,
    #     "threshold": 4,
    #     # [2,5], [5,6], [6,10], [10,13], [13,8]
    #     "want": 6,
    # },
    {
        "name": "k < len(arr), multi match threshold",
        "arr": [11, 13, 17, 23, 29, 31, 7, 5, 2, 3],
        "k": 3,
        "threshold": 5,
        # [2,5], [5,6], [6,10], [10,13], [13,8]
        "want": 6,
    },
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_num_of_sub_arrays(testCase):
    arr: List[int] = testCase["arr"]
    k: int = testCase["k"]
    threshold: int = testCase["threshold"]

    want: int = testCase["want"]

    sol = Solution()
    got = sol.numOfSubarrays(arr, k, threshold)
    assert got == want
