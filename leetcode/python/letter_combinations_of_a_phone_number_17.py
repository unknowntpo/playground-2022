from typing import List
import pytest

"""
LeetCode 17: Letter Combinations of a Phone Number

Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.

A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.

2: abc
3: def
4: ghi
5: jkl
6: mno
7: pqrs
8: tuv
9: wxyz

Example 1:
Input: digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]

Example 2:
Input: digits = ""
Output: []

Example 3:
Input: digits = "2"
Output: ["a","b","c"]
"""


class Solution:
    arr = []

    def letterCombinations(self, digits: str) -> List[str]:
        if len(digits) == 0:
            return []
        m = {
            "2": "abc",
            "3": "def",
            "4": "ghi",
            "5": "jkl",
            "6": "mno",
            "7": "pqrs",
            "8": "tuv",
            "9": "wxyz",
        }
        arr = []
        res = []
        n = len(digits)

        def bt():
            pos = len(arr)
            if pos == n:
                res.append("".join(arr))
                return

            for c in m[digits[pos]]:
                arr.append(c)
                bt()
                arr.pop()

        bt()

        return res


testCases = [
    {
        "name": "example 1",
        "digits": "23",
        "want": ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"],
    },
    {"name": "example 2", "digits": "", "want": []},
    {"name": "example 3", "digits": "2", "want": ["a", "b", "c"]},
    # 2 ->abc, 3 -> def 4 -> ghi
    # {"name": "3 digits", "digits": "234", "want": ["a", "b", "c"]},
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
        for method_name in ["letterCombinations"]
    ],
    ids=id_func,
)
def test_letter_combinations(testCase, method_name):
    digits: str = testCase["digits"]
    want: List[str] = testCase["want"]

    sol = Solution()
    fn = getattr(sol, method_name)
    got = fn(digits)

    # Sort both lists to ensure order doesn't matter
    assert sorted(got) == sorted(want)
