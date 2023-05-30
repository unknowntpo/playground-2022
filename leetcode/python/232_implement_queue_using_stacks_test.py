import pytest
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def postOrderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        return []


testCases = [
    {
        "root": TreeNode(1, None, TreeNode(2, TreeNode(3))),
        "want": [3, 2, 1],
    },
    {
        "root": None,
        "want": [],
    },
]


@pytest.mark.parametrize("testCase", testCases)
def test_postOrderTraversal(testCase):
    sol = Solution()
    root = testCase["root"]
    want = testCase["want"]
    assert sol.postOrderTraversal(root) == want
