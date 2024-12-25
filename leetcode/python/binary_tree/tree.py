from __future__ import annotations
from typing import Optional, Deque, List, TYPE_CHECKING
from collections import deque

class BinaryTree[T]:
    def __init__(self, root: Optional[TreeNode[T]] = None):
        self.root = root

    @staticmethod
    def show_tree(tree:  BinaryTree[T]):
        q: List[Optional[TreeNode[T]]] = [tree.root]
        while q:
            nodes: List[Optional[TreeNode[T]]] = []
            for n in q:
                print(n.val if n is not None else "x", end = " ")
                if n is not None:
                    nodes.append(n.left)
                    nodes.append(n.right)
            q = nodes
            print()

    @staticmethod
    def build_tree(vals: List[Optional[int]]) -> BinaryTree[T]:
        if len(vals) == 0 or vals[0] is None:
            raise Exception("root can not has 0 element or root[0] can not be None")
        root = TreeNode()
        dq: Deque[Optional[T]] = deque(vals)
        root.val = dq.popleft()
        BinaryTree.__build_tree(root, dq)

        return BinaryTree(root)

    @staticmethod
    def __build_tree(root: TreeNode, dq: Deque[Optional[int]]):
        if len(dq) == 0:
            return
        lVal: Optional[T] = None
        rVal: Optional[T] = None
        if len(dq) > 0:
            lVal = dq.popleft()
        if len(dq) > 0:
            rVal = dq.popleft()

        if lVal is not None:
            root.left = TreeNode(val=lVal)
            BinaryTree.__build_tree(root.left, dq)
        if rVal is not None:
            root.right = TreeNode(val=rVal)
            BinaryTree.__build_tree(root.right, dq)

    def to_array(self) -> List[Optional[T]]:
        out: List[Optional[T]] = []
        dq: Deque[TreeNode[T]] = deque([self.root])

        """
        out: [3, 9, 20]
        """
        while dq:
            node: TreeNode[T] = dq.popleft()
            out.append(node.val)
            if node.left is not None:
                dq.append(node.left)
            else:
                out.append(None)
            if node.right is not None:
                dq.append(node.right)
            else:
                out.append(None)

        return out


class TreeNode[T]:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

