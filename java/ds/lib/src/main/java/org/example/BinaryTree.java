package org.example;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class BinaryTree<T> {
    private TreeNode<T> root;

    private BinaryTree(TreeNode<T> root) {
        this.root = root;
    }

    public void setRoot(TreeNode<T> root) {
        this.root = root;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    public static <T> BinaryTree<T> createBinaryTree(T[] vals) {
        if (vals == null || vals.length == 0) {
            throw new IllegalArgumentException("invalid vals, vals should be non-zero length");
        }

        // 0 < vals.length < n, and vals.length % 2 == 0
        Queue<TreeNode<T>> queue = new LinkedList<>();
        TreeNode<T> root = new TreeNode<T>(vals[0], null, null);
        queue.offer(root);

        for (int i = 1; i < vals.length; i += 2) {
            TreeNode<T> curNode = queue.poll();

            System.out.printf("i: %s, i+1: %s%n",
                    (vals[i] != null) ? vals[i] : "null",
                    (vals[i + 1] != null) ? vals[i + 1] : "null");

            if (curNode == null) {
                throw new RuntimeException("curNode should not be null");
            }

            if (vals[i] != null) {
                curNode.setLeft(new TreeNode<>(vals[i], null, null));
                queue.offer(curNode.getLeft());
            }

            if (i + 1 < vals.length && vals[i + 1] != null) {
                curNode.setRight(new TreeNode<>(vals[i + 1], null, null));
                queue.offer(curNode.getRight());
            }
        }

        return new BinaryTree<T>(root);
    }

    public void preOrderTraversal(BinaryTree<T> tree, Consumer<TreeNode<T>> nodeProcessor) {
        if (tree.getRoot() == null) {
            return;
        }
        this._preOrderTraversal(this.getRoot(), nodeProcessor);
    }

    private void _preOrderTraversal(TreeNode<T> node, Consumer<TreeNode<T>> nodeProcessor) {
        if (node == null) {
            return;
        }

        nodeProcessor.accept(node);
        this._preOrderTraversal(node.getLeft(), nodeProcessor);
        this._preOrderTraversal(node.getRight(), nodeProcessor);
    }

    public void preOrderIterUniversalTraversal(BinaryTree<T> tree, Consumer<TreeNode<T>> nodeProcessor) {
        if (tree.getRoot() == null) {
            return;
        }
        var stack = new Stack<TreeNode>();
        stack.push(tree.getRoot());

        while (!stack.isEmpty()) {
            TreeNode<T> node = stack.pop();
            if (node != null) {
                if (node.getRight() != null) stack.push(node.getRight());
                if (node.getLeft() != null) stack.push(node.getLeft());

                // do operation on current node
                stack.push(node);
                stack.push(null);
            } else {
                node = stack.pop();
                nodeProcessor.accept(node);
            }
        }
    }
}
