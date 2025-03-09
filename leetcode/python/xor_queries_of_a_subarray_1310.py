from typing import List
import pytest

"""
LeetCode 1310: XOR Queries of a Subarray

You are given an array arr of positive integers and an array queries where queries[i] = [Li, Ri].

For each query i, compute the XOR of elements from Li to Ri (inclusive, 0-indexed).

Return an array containing the result for each query.

示例 1：

输入：arr = [1,3,4,8], queries = [[0,1],[1,2],[0,3],[3,3]]
输出：[2,7,14,8] 
解释：
数组中元素的二进制表示形式是：
1 = 0001 
3 = 0011 
4 = 0100 
8 = 1000 
查询的 XOR 值为：
[0,1] = 1 xor 3 = 2 
[1,2] = 3 xor 4 = 7 
[0,3] = 1 xor 3 xor 4 xor 8 = 14 
[3,3] = 8
示例 2：

输入：arr = [4,8,2,10], queries = [[2,3],[1,3],[0,0],[0,3]]
输出：[8,0,4,4]
"""


class Solution:
    def xorQueries(self, arr: List[int], queries: List[List[int]]) -> List[int]:
        # TODO: Implement this method
        """
        [1,3,4,8]
        [0,1,2,6,14]
        0010 0100
        0100 = 0110 =
        0110 xor 1000 = 1110
        1 xor 3 = 0001 xor 0011 = 2
        2 xor 4 = 0010 xor 0100 = 0110 = 6
        6 xor 8 = 0110 xor 1000 = 1110 = 14

        [1,2] = 3 xor 4 = s[3] ? s[1] = 6 ? 1 = 0110 xor 0001 = 7
        [0,3] = s[4] xor s[0] = 14 xor 0 = 1110 xor 0000 = 0110 = 14

        arr = [4,8,2,10]
        s = [0, 4, 12, 14, 4]
        s[1] = 0 xor 4 = 4
        s[2] = 4 xor 8 = 0100 xor 1000 = 1100 = 12
        s[3] = 12 xor 2 = 1100 xor 0010 = 1110 = 14
        s[4] = 14 xor 10 = 1110 xor 1010 = 0100 = 4

        """
        n = len(arr)
        s = [0] * (n + 1)
        for i in range(n):
            s[i + 1] = s[i] ^ arr[i]

        print(s)

        out = []
        for l, r in queries:
            out.append(s[r + 1] ^ s[l])
        return out


testCases = [
    {
        "name": "example 1",
        "arr": [1, 3, 4, 8],
        "queries": [[0, 1], [1, 2], [0, 3], [3, 3]],
        "want": [2, 7, 14, 8],
    },
    {
        "name": "example 2",
        "arr": [4, 8, 2, 10],
        "queries": [[2, 3], [1, 3], [0, 0], [0, 3]],
        "want": [8, 0, 4, 4],
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
        for method_name in ["xorQueries"]
    ],
    ids=id_func,
)
def test_xor_queries(testCase, method_name):
    arr: List[int] = testCase["arr"]
    queries: List[List[int]] = testCase["queries"]
    want: List[int] = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(arr, queries)
    assert got == want
