from collections import deque

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
    def sumOfLeftLeaves(self, root: TreeNode):
        print(root)
        return 0

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

"""

 i
vals = [3,9,20,None,None,5,7]

q: containing nodes
q: [node]

1. enqueue root
2. pop root, set val
3. set root.left, root.right

root: TreeNode()
root.val = vals.pop()
nodeStack.push(root.right)
nodeStack.push(root.left)

[rootLeft, rootRight]

while 1:
    node = nodeStack.pop()
    val = q.pop()
    
    node.val = val
    nodeStack.push(node.right)
    nodeStack.push(node.left)
    
_buildTree(root):
    if !vals:
        # no more val to build
        return
    lVal = vals.pop()
    rVal = vals.pop()
    if lVal is not None:
        root.left = TreeNode(val=lVal)
        buildTree(root.left)
    if rVal is not None:
       root.right = TreeNode(val=rVal)
       buildTree(root.right)
     
vals = [3,9,20,None,None,5,7]

root: 3

vals = [9,20,None,None,5,7]

lVal = 9
rVal = 20

    3 
  9    20
  
vals = [None,None, 5, 7]

buildTree(9)


    



"""



testCases = [
    {"name": "case0", "vals": [3,9,20, None, None, 5, 7], "want": 24},
    {"name": "case1", "vals": [1], "want": 0},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_sum_of_left_root(testCase):
    vals = testCase["vals"]
    want = testCase["want"]

    sol = Solution()
    root = sol.build_tree(vals)

    got = sol.sumOfLeftLeaves(root)
    assert got == want
