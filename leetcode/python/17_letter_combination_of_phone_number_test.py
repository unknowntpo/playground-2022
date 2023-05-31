import pytest
from typing import *

phone_mapping = {
    2: 'abc',
    3: 'def',
    4: 'ghi',
    5: 'jkl',
    6: 'mno',
    7: 'pqrs',
    8: 'tuv',
    9: 'wxyz'
}


class Solution:
    def __init__(self):
        self.out = []
        self.path = []

    def letterCombinations(self, digits: str) -> List[str]:
        if len(digits) == 0:
            return []
        self.dp(digits, 0)
        return self.out

    def dp(self, digits: str, cur: int):
        if len(self.path) == len(digits):
            self.out.append(self.path[:])
            return

        letter = phone_mapping[int(digits[cur])]

        for e in letter:
            self.path.append(e)
            self.dp(digits, cur+1)
            self.path.pop()


testCases = [
    {
        "name": "23",
        "input": "23",
        "want": [['a', 'd'], ['a', 'e'], ['a', 'f'], ['b', 'd'], ['b', 'e'], ['b', 'f'], ['c', 'd'], ['c', 'e'], ['c', 'f']],
    },
    {
        "name": "2",
        "input": "2",
        "want": [['a'], ['b'], ['c']],
    },
    {
        "name": "<None>",
        "input": "",
        "want": [],
    },
]


@ pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_postOrderTraversal(testCase):
    sol = Solution()
    input = testCase["input"]
    want = testCase["want"]
    assert sol.letterCombinations(input) == want
