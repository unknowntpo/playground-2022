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
给定二叉树的根节点 root ，返回所有左叶子之和。

提示:

    节点数在 [1, 1000] 范围内
    -1000 <= Node.val <= 1000

Ref: https://leetcode.cn/problems/sum-of-left-leaves/description/
"""

class Solution:
    def sumOfLeftLeaves(self, root: TreeNode) -> int:
        # is left leaf ?
        # if n.left is not None and n.left.left == n.left.right == None
        # is leaf ?
        # node.left == node.right == None
        s = 0
        if root.left is not None and root.left.left == root.left.right is None:
            s+= root.left.val
        if root.right is not None:
            s += self.sumOfLeftLeaves(root.right)
        return s

    def show_tree(self, root:  TreeNode):
        q: List[Optional[TreeNode]] = [root]
        while q:
            nodes: List[Optional[TreeNode]] = []
            for n in q:
                print(n.val if n is not None else "x", end = " ")
                if n is not None:
                    nodes.append(n.left)
                    nodes.append(n.right)
            q = nodes
            print()


    def build_tree(self, vals: List[Optional[int]]) -> TreeNode:
        if len(vals) == 0 or vals[0] is None:
            raise Exception("root can not has 0 element or root[0] can not be None")
        root = TreeNode()
        dq: Deque[Optional[int]] = deque(vals)
        root.val = dq.popleft()
        self.__build_tree(root, dq)

        return root

    def __build_tree(self, root: TreeNode, dq: Deque[Optional[int]]):
        if len(dq) == 0:
            return
        lVal: Optional[int] = None
        rVal: Optional[int] = None
        if len(dq) > 0:
            lVal = dq.popleft()
        if len(dq) > 0:
            rVal = dq.popleft()

        if lVal is not None:
            root.left = TreeNode(val=lVal)
            self.__build_tree(root.left, dq)
        if rVal is not None:
            root.right = TreeNode(val=rVal)
            self.__build_tree(root.right, dq)

testCases = [
    {"name": "case0", "vals": [3,9,20, None, None, 15, 7], "want": 24},
    {"name": "case1", "vals": [1], "want": 0},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_sum_of_left_root(testCase):
    vals = testCase["vals"]
    want = testCase["want"]

    sol = Solution()
    root = sol.build_tree(vals)
    sol.show_tree(root)

    got = sol.sumOfLeftLeaves(root)
    assert got == want
