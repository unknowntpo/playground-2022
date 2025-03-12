from typing import List
import pytest

"""
LeetCode 78: Subsets

Given an integer array nums of unique elements, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.

Example 1:
Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]

Example 2:
Input: nums = [0]
Output: [[],[0]]
"""


class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        # TODO: Implement this method
        n = len(nums)
        arr = []
        res = []

        def bt(pos):
            if pos == n:
                res.append(arr[:])
                return
            # choose
            arr.append(nums[pos])
            bt(pos + 1)
            # no choose
            arr.pop()
            bt(pos + 1)

        bt(0)
        return res


testCases = [
    {
        "name": "example 1",
        "nums": [1, 2, 3],
        "want": [[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]],
    },
    {"name": "example 2", "nums": [0], "want": [[], [0]]},
    {
        "name": "testcase0",
        "nums": [2, 3],
        "want": [[], [2], [2, 3], [3]],
    },
]


def id_func(param):
    if isinstance(param, dict):
        return param["name"]
    return param


@pytest.mark.parametrize(
    "testCase,method_name",
    [(testCase, method_name) for testCase in testCases for method_name in ["subsets"]],
    ids=id_func,
)
def test_subsets(testCase, method_name):
    nums: List[int] = testCase["nums"]
    want: List[List[int]] = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(nums)

    # Sort both lists to ensure order doesn't matter
    # First sort each subset
    got_sorted = [sorted(subset) for subset in got]
    want_sorted = [sorted(subset) for subset in want]
    # Then sort the list of subsets
    assert sorted(map(tuple, got_sorted)) == sorted(map(tuple, want_sorted))
