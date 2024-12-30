from __future__ import annotations
from typing import Optional, Deque, List, Callable
from collections import deque


class BinaryTree[T]:
    def __init__(self, root: Optional[TreeNode[T]] = None):
        self.root = root

    def show_tree(self):
        q: List[Optional[TreeNode[T]]] = [self.root]
        while q:
            nodes: List[Optional[TreeNode[T]]] = []
            for n in q:
                print(n.val if n is not None else "x", end=" ")
                if n is not None:
                    nodes.append(n.left)
                    nodes.append(n.right)
            q = nodes
            print()

    def prefix_iter_traversal(self, f: Callable[[TreeNode[T]], None]):
        """
           3
        9   20
           15 7

        """
        if self.root is None:
            return

        dq: Deque[Optional[TreeNode[T]]] = deque([self.root])
        # dq: [3]
        while dq:
            node = dq.pop()
            # dq: [], node: 3
            if node is not None:
                print("node is not None: ", node.val)
                if node.right is not None:
                    dq.append(node.right)
                    # dq: [20]
                if node.left is not None:
                    dq.append(node.left)
                    # dq: [20 9]
                dq.append(node)
                dq.append(None)
                # dq: [20 9 3 None]
            else:
                # for n in dq: print(f"{n.val if n is not None else "None"}")
                node = dq.pop()
                # dq: [20 9]
                # f(3)
                f(node)
                # print("cur")
        return

    @staticmethod
    def build_tree(vals: List[Optional[int]]) -> BinaryTree[T]:
        if len(vals) == 0 or vals[0] is None:
            return BinaryTree(None)
        root = TreeNode()
        dq: Deque[Optional[T]] = deque(vals)
        BinaryTree.__build_tree_iter(root, dq)

        return BinaryTree(root)
    @staticmethod
    def __build_tree_iter(root: Optional[TreeNode[T]], vals: Deque[Optional[int]])-> BinaryTree[T]:
        """
        [3,9,20, None, None, 15, 7, None, None, None, None]
        """
        if len(vals) == 0:
            return

        root.val = vals.popleft()

        dq = deque([root])

        while vals:
            node = dq.popleft()

            if len(vals) == 0:
                break
            node.left = TreeNode(vals.popleft())
            if len(vals) == 0:
                break
            node.right = TreeNode(vals.popleft())

            if node.left is not None:
                dq.append(node.left)
            if node.right is not None:
                dq.append(node.right)

        return BinaryTree(root)



    @staticmethod
    def __build_tree_recr_failed(root: TreeNode, dq: Deque[Optional[int]]):
        """
        [3,9,20, None, None, 15, 7, None, None, None, None]
        """
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
        dq: Deque[Optional[TreeNode[T]]] = deque([self.root])

        """
        out: [3, 9, 20]
            3
           9 20
         x x 15 7
        """
        while dq:
            node: Optional[TreeNode[T]] = dq.popleft()
            if node is None:
                # if same layer has not None node, then None should be added to out
                if len(list(filter(lambda node: node is not None, dq))) > 0:
                    out.append(None)
                break

            # append non None node.val
            out.append(node.val)

            # enqueue next layer
            dq.append(node.left)
            dq.append(node.right)

        return out

    def prefix_recursive_traversal(self, f: Callable[[Optional[TreeNode[T]]], None]):
        if self.root is None:
            return

        def _walk(
            root: Optional[TreeNode[T]], f: Callable[[Optional[TreeNode[T]]], None]
        ):
            if root is None:
                return
            f(root)
            _walk(root.left, f)
            _walk(root.right, f)

        _walk(self.root, f)
    def level_order_iter_traversal(self, f: Callable[[Optional[TreeNode[T]]], None]):
        if self.root is None:
            return

        dq: Deque[Optional[TreeNode[T]]] = deque([self.root])

        while dq:
            node = dq.popleft()
            f(node)
            if node.left is not None:
                dq.append(node.left)
            if node.right is not None:
                dq.append(node.right)


        # def _walk(
        #     root: Optional[TreeNode[T]], f: Callable[[Optional[TreeNode[T]]], None]
        # ):
        #     if root is None:
        #         return
        #     f(root)
        #     _walk(root.left, f)
        #     _walk(root.right, f)

        # _walk(self.root, f)



class TreeNode[T]:
    def __init__(self, val=0, left: Optional[TreeNode[T]]=None, right: Optional[TreeNode[T]]=None):
        self.val = val
        self.left = left
        self.right = right
