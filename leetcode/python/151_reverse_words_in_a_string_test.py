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
    {
        "name": "has trailing space",
        "input": "hello world  ",
        "want": "hello world",
    },
    {
        "name": "has trailing space",
        "input": "h ",
        "want": "h",
    },
]


@ pytest.mark.parametrize("testCase", removeWhiteSpaceTestCases, ids=lambda testCase: testCase["name"])
def test_removeWhiteSpace(testCase):
    input = testCase["input"]
    want = testCase["want"]
    assert removeWhiteSpacePrefix(input) == want


def removeWhiteSpacePrefix(input: str) -> str:
    s = list(input)
    i = 0
    # trim precending white space
    while s[i] == ' ':
        i += 1

    s = s[i:]
    for j in range(i, len(s)):
        if s[j] != ' ' or (s[j] == ' ' and s[j-1] != ' '):
            s[i] = s[j]
            i += 1

    # trim trailing white space
    if s[i-1] == ' ' and s[i-2] != ' ':
        i -= 1

    return ''.join(s[:i])


"""
i: hold the correct answer

j: fetch the word

i
  j
__ab__b_c_

  i
    j
abab__b_c_

      i
         j
ab_b_c__c_


"""
