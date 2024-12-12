import pytest
from typing import List

testCases = [
    {"name": "case0", "input": [7, 1, 5, 3, 6, 4], "want": 5},
    {
        "name": "case1",
        "input": [7, 6, 4, 3, 1],
        "want": 0,
    },
]

"""
You are given an array prices where prices[i] is the price of a given stock on the ith day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

Ref: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/ 
"""


class Solution:
    # input: [1,2,3,4,5]
    def maxProfit(self, prices: List[int]) -> int:
        maxProfit = 0
        #
        for i in range(0, len(prices) - 1):
            # invariant: 0 < i < j < len(prices)
            # i = 0, prices[i] = 1
            for j in range(i + 1, len(prices)):
                # j = 1, prices[i] = 2
                curProfit = prices[j] - prices[i]
                # curProfit = 2 - 1 = 1
                if curProfit > maxProfit:
                    # 2 - 1 = 1
                    maxProfit = curProfit
                    # update maxProfit = 1
        return maxProfit


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_maxProfit(testCase):
    sol = Solution()
    input = testCase["input"]
    want = testCase["want"]
    got = sol.maxProfit(input)
    assert got == want
