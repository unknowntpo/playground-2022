from collections import deque
from os import times_result

import pytest
from typing import List, Optional, Deque

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

"""
给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。

提示：

    树中节点数目范围在 [0, 100] 内
    -100 <= Node.val <= 100

Ref: https://leetcode.cn/problems/invert-binary-tree/description/
"""
class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        pass

testCases = [
    {"name": "case0", "root": [2,1,3], "want": [2,3,1]},
    # {"name": "case0", "root": [4,2,7,1,3,6,9], "want": 24},
    {"name": "case1", "root": [1], "want": 0},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_invert_binary_tree(testCase):
    vals = testCase["vals"]
    want = testCase["want"]

    sol = Solution()
    root = sol.build_tree(vals)
    sol.show_tree(root)

    got = sol.invertTree(root)
    assert got == want
