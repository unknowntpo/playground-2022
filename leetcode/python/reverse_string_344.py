import pytest
from typing import List

testCases = [
    {"name": "length-1", "input": ["a"], "want": ["a"]},
    {
        "name": "length-5",
        "input": ["h", "e", "l", "l", "o"],
        "want": ["o", "l", "l", "e", "h"],
    },
]

"""
Write a function that reverses a string.
The input string is given as an array of characters s.
You must do this by modifying the input array in-place with O(1) extra memory.

Constraints:
    1 <= s.length <= 105
    s[i] is a printable ascii character.

Ref: https://leetcode.com/problems/reverse-string/description/
"""


class Solution:
    def reverseString(self, s: List[str]) -> None:
        print("Hlelo")
        """
        Do not return anything, modify s in-place instead.

        # odd
         i
               j
        [1, 3, 4]
            i
            j
        [4, 3, 1]

         i
            j
        [0, 1]

            i
         j
        [1, 0]


        # even
         i
                  j
        [1, 3, 4, 5]
            i .
               j
        [5, 3, 4, 1]
        """

        i = 0
        j = len(s) - 1

        while i < j:
            print("i, j, s", i, j, s)
            s[i], s[j] = s[j], s[i]
            i += 1
            j -= 1


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_reverseString(testCase):
    sol = Solution()
    input = testCase["input"]
    want = testCase["want"]
    sol.reverseString(input)
    assert input == want
