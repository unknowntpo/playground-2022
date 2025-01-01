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
给你一个二叉树的根节点 root ， 检查它是否轴对称。

输入：root = [1,2,2,3,4,4,3]
输出：true

输入：root = [1,2,2,null,3,null,3]
输出：false

提示：

树中节点数目在范围 [1, 1000] 内
-100 <= Node.val <= 100

Ref: https://leetcode.cn/problems/symmetric-tree/description/
"""
class Solution:
    def isSymmetric(self, root: Optional[TreeNode]) -> bool:
        # [1,2,2,None,3,None,3]
        # [1,2,2,3,4,4,3]
        nodes = [root]
        # nodes: [1]
        isRoot = True
        while nodes:
            print(' '.join(str(node.val) if node else 'None' for node in nodes))
            # [1]
            # nodes: [2, 2]
            # nodes: [3,4,4,3]
            if not isRoot and len(nodes) % 2 != 0: return False
            # Special case for root node
            isRoot = False

            i, j = 0, len(nodes) - 1
            # i = 0, j = 0
            # i = 0, j = 1
            # i = 0, j = 3
            while i < j:
                left, right = nodes[i], nodes[j]
                if left is None and right is None:
                    i+=1
                    j-=1
                    continue
                if left is None or right is None: return False

                if left.val != right.val: return False
                i+=1
                j-=1

            nextLayerNodes = []
            for i in range(0, len(nodes)):
                if nodes[i] is not None:
                    nextLayerNodes.append(nodes[i].left)
                    nextLayerNodes.append(nodes[i].right)
                    # dq: [2, 2]
                    # nextLayerNodes: [3, 4]
                    # nextLayerNodes: [3,4,4,3]
                    # nextLayerNodes: [x, x]
                    # nextLayerNodes: [x, x]
            nodes = nextLayerNodes
            # nodes: [3,4,4,3]
        return True


testCases = [
    {"name": "is symmetric", "root": [1,2,2,3,4,4,3], "want": True},
    {"name": "not symmetric", "root": [1,2,2,None,3,None,3], "want": False},
    {"name": "only root node should returns True", "root": [1], "want": True},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_is_symmetric(testCase):
    root: List[Optional[int]] = testCase["root"]
    want: bool = testCase["want"]

    tree = BinaryTree.build_tree(root)

    sol = Solution()
    res = sol.isSymmetric(tree.root)
    assert res == want
