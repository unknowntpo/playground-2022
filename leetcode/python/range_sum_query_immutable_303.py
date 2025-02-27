from typing import List
import pytest

"""
给定一个整数数组  nums，处理以下类型的多个查询:

计算索引 left 和 right （包含 left 和 right）之间的 nums 元素的 和 ，其中 left <= right
实现 NumArray 类：

NumArray(int[] nums) 使用数组 nums 初始化对象
int sumRange(int i, int j) 返回数组 nums 中索引 left 和 right 之间的元素的 总和 ，包含 left 和 right 两点（也就是 nums[left] + nums[left + 1] + ... + nums[right] )
 

示例 1：

输入：
["NumArray", "sumRange", "sumRange", "sumRange"]
[[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
输出：
[null, 1, -1, -3]

解释：
NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
numArray.sumRange(0, 2); // return 1 ((-2) + 0 + 3)
numArray.sumRange(2, 5); // return -1 (3 + (-5) + 2 + (-1)) 
numArray.sumRange(0, 5); // return -3 ((-2) + 0 + 3 + (-5) + 2 + (-1))
"""


class NumArray:
    def __init__(self, nums: List[int]):
        """
        # sum from [0, i)
        # prefix[i] = prefix[i - 1] + nums[i - 1]
        # sumRange(0, 2) = prefix(2 + 1) - prefix(0)
        # 0, 1, 2

        [-2, 0, 3, -5, 2, -1]
        preSumArr:
        [0, -2, -2, 1, -4, -2, -3]
        sumRange(0, 2) = prefix[2 + 1] - prefix(0) = 1 - 0 = 1
        sumRange(2, 5) = prefix[5 + 1] - prefix[2] = -3 -(-2) = -1

        """
        n = len(nums)
        self.preSumArr = [0] * (n + 1)
        # TODO: Implement initialization
        for i in range(1, n + 1):
            self.preSumArr[i] = self.preSumArr[i - 1] + nums[i - 1]

    def sumRange(self, left: int, right: int) -> int:
        # TODO: Implement sumRange method
        return self.preSumArr[right + 1] - self.preSumArr[left]


testCases = [
    {
        "name": "example from description",
        "nums": [-2, 0, 3, -5, 2, -1],
        "queries": [
            {"left": 0, "right": 2, "want": 1},
            {"left": 2, "right": 5, "want": -1},
            {"left": 0, "right": 5, "want": -3},
        ],
    }
]


def id_func(param):
    if isinstance(param, dict):
        return param["name"]
    return param


@pytest.mark.parametrize(
    "testCase",
    testCases,
    ids=id_func,
)
def test_num_array(testCase):
    nums = testCase["nums"]
    queries = testCase["queries"]

    num_array = NumArray(nums)

    for query in queries:
        left = query["left"]
        right = query["right"]
        want = query["want"]

        got = num_array.sumRange(left, right)
        assert got == want, f"sumRange({left}, {right}) = {got}, want {want}"
