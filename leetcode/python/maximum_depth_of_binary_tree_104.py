from collections import deque
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree, TreeNode

"""
给定一个二叉树 root ，返回其最大深度。

二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。

树中节点的数量在 [0, 104] 区间内。
-100 <= Node.val <= 100

Ref: https://leetcode.cn/problems/maximum-depth-of-binary-tree/description/ 
"""


class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        """
         r (cur)
        r.l r.r
                3 (cur: 0)
             9 .(cur=1)  20 (2)
            n (cur=2) n (cur=2) . 15 (cur=2) 7 (cur=2)
                                x(cur=3) .  x (cur=3)
        """
        if root is None:
            return 0

        def _maxDepth(curDepth: int, root: Optional[TreeNode]) -> int:
            if root is None:
                return curDepth
            return max(
                _maxDepth(curDepth + 1, root.left), _maxDepth(curDepth + 1, root.right)
            )

        return _maxDepth(0, root)


testCases = [
    {"name": "3 layers", "root": [3, 9, 20, None, None, 15, 7], "want": 3},
    {"name": "a node has one None node", "root": [2, 4, 5, None, 3, 15, 7], "want": 3},
    {"name": "only two layers", "root": [1, None, 2], "want": 2},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_invert_binary_tree(testCase):
    root: List[int] = testCase["root"]
    want: List[int] = testCase["want"]

    tree = BinaryTree.build_tree(root)

    sol = Solution()
    # resRoot = sol.invertTree(tree.root)
    res = sol.maxDepth(tree.root)
    assert res == want
