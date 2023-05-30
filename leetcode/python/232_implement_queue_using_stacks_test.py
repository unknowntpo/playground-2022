import pytest
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def postOrderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        if root is None:
            return []
        visited = [False]
        stack = [root]
        out = []
        while stack:
            node = stack.pop()
            v = visited.pop()
            if v is True:
                out.append(node.val)
            else:
                stack.append(node)
                visited.append(True)
                if node.right is not None:
                    stack.append(node.right)
                    visited.append(False)
                if node.left is not None:
                    stack.append(node.left)
                    visited.append(False)
        return out


testCases = [
    {
        "name": "Tree: [1]",
        "root": TreeNode(1),
        "want": [1],
    },
    {
        "name": "Tree: [1, 2, 3]",
        "root": TreeNode(1, TreeNode(2), TreeNode(3)),
        "want": [2, 3, 1],
    },
    {
        "name": "Tree: [1, x, 2, 3 ,x]",
        "root": TreeNode(1, None, TreeNode(2, TreeNode(3))),
        "want": [3, 2, 1],
    },
    {
        "name": "Tree: []",
        "root": None,
        "want": [],
    },
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_postOrderTraversal(testCase):
    sol = Solution()
    root = testCase["root"]
    want = testCase["want"]
    assert sol.postOrderTraversal(root) == want
