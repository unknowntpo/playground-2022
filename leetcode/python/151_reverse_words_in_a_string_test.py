import pytest
from typing import *


class Solution:
    def reverseWords(self, s: str) -> str:
        s = removeWhiteSpacePrefix(s)
        return s


testCases = [
    {
        "name": "no precending space",
        "input": "the sky is blue",
        "want": "blue is sky the",
    },
    {
        "name": "has precending space",
        "input": "  hello world",
        "want": "world hello",
    },
]


@ pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_reverseWords(testCase):
    sol = Solution()
    input = testCase["input"]
    want = testCase["want"]
    assert sol.reverseWords(input) == want


removeWhiteSpaceTestCases = [
    {
        "name": "no precending space",
        "input": "the sky is blue",
        "want": "the sky is blue",
    },
    {
        "name": "has precending space",
        "input": "  hello world",
        "want": "hello world",
    },
]


@ pytest.mark.parametrize("testCase", removeWhiteSpaceTestCases, ids=lambda testCase: testCase["name"])
def test_removeWhiteSpace(testCase):
    input = testCase["input"]
    want = testCase["want"]
    assert removeWhiteSpacePrefix(input) == want


def removeWhiteSpacePrefix(s: str) -> str:
    i = 0
    while (s[i] == " "):
        i += 1
    return s[i:]
