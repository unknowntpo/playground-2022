package com.leetcode;

import com.leetcode.BinaryTree.TreeNode;
import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * 给定一个二叉树 root ，返回其最大深度。
 * <p>
 * 二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。
 * <p>
 * 示例 1：
 * <p>
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：3
 * 示例 2：
 * <p>
 * 输入：root = [1,null,2]
 * 输出：2
 */
public class BinaryTreeRightSideView199 {
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
        public List<Integer> rightSideView(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            Deque<TreeNode> nodesInLayer = new LinkedList<>();
            nodesInLayer.add(root);

            List<Integer> results = new ArrayList<>();

            Deque<TreeNode> tempList = new LinkedList<>();
            while (!nodesInLayer.isEmpty()) {
                TreeNode node = nodesInLayer.pop();

                if (node.left != null) {
                    tempList.add(node.left);
                }
                if (node.right != null) {
                    tempList.add(node.right);
                }

                // if is last in layer, means it's the right side view
                if (nodesInLayer.isEmpty()) {
                    results.add(node.val);
                    Deque<TreeNode> tmp = nodesInLayer;
                    nodesInLayer = tempList;
                    tempList = tmp;
                }
            }

            return results;
        }

        public List<Integer> rightSideView2(TreeNode root) {
            ArrayList<Integer> results = new ArrayList<>();

            BiConsumer<TreeNode, Integer>[] fn = new BiConsumer[1];

            // FIXME: fn interface with multi args
            fn[0] = (node, depth) -> {
                if (node == null) {
                    return;
                }
                if (depth == results.size()) {
                    results.add(node.val);
                }
                fn[0].accept(node.right, depth + 1);
                fn[0].accept(node.left, depth + 1);
            };

            fn[0].accept(root, 0);

            return results;
        }
    }
}
