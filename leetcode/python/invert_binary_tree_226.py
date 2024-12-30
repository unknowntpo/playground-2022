from collections import deque
from os import times_result

import pytest
from typing import List, Optional, Deque

from binary_tree.tree import BinaryTree

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
    def invertTree_iter(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        """
            3
          4  5
         6 2 8 1
        """
        if root is None: return None

        q = deque([root])

        while q:
            node = q.popleft()
            node.left, node.right = node.right, node.left
            if node.left is not None:
                q.append(node.left)
            if node.right is not None:
                q.append(node.right)

        return root

    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        """

           2
          1  3
        """
        if root is None: return None

        print(f"""before flip, root: {root.val}, root.left: {root.left.val if root.left is not None else 'None'},
              root.right: {root.right.val if root.right is not None else 'None'}""")

        root.left, root.right = root.right, root.left

        print(f"""after flip, root: {root.val}, root.left: {root.left.val if root.left is not None else 'None'},
              root.right: {root.right.val if root.right is not None else 'None'}""")

        if root.left is not None:
            root.left = self.invertTree(root.left)
        if root.right is not None:
            root.right = self.invertTree(root.right)
        return root

testCases = [
    {"name": "case0", "root": [2,1,3], "want": [2,3,1]},
    # {"name": "case0", "root": [4,2,7,1,3,6,9], "want": 24},
    {"name": "only one node", "root": [1], "want": [1]},
    {"name": "no node", "root": [], "want": []},
    {"name": "3 layers", "root": [4,2,7,1,3,6,9], "want":[4,7,2,9,6,3,1]},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_invert_binary_tree(testCase):
    root: List[int] = testCase["root"]
    want: List[int] = testCase["want"]

    tree = BinaryTree.build_tree(root)

    sol = Solution()
    # resRoot = sol.invertTree(tree.root)
    resRoot = sol.invertTree_iter(tree.root)
    res = []
    BinaryTree(resRoot).level_order_iter_traversal(lambda node: res.append(node.val))
    assert res == want
