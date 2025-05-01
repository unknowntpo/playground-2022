package com.leetcode;

import com.leetcode.BinaryTree.TreeNode;

public class InvertBinaryTree226 {
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    static class Solution {
        public TreeNode invertTree(TreeNode root) {
            if (root == null) {
                return root;
            }
            final TreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;
            if (root.left != null) {
                this.invertTree(root.left);
            }
            if (root.right != null) {
                this.invertTree(root.right);
            }
            return root;
        }
    }
}
