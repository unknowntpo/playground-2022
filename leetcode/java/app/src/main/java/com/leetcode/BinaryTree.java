package com.leetcode;

import com.sun.source.tree.Tree;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class BinaryTree {
    private TreeNode root;

    public BinaryTree() {
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    public static BinaryTree of(List<Integer> nums) {
        // build tree from array
        BinaryTree binaryTree = new BinaryTree();
        return binaryTree.buildTreeFromList(nums);
    }

    private BinaryTree buildTreeFromList(List<Integer> nums) {
        /**
         * tree
         * nums: [1,2,3], []
         * if n.size() == 0 return null root BinaryTree
         * root = Node(nums[0])
         * q: [root]
         * idx = 1
         * while idx < nums.size():
         *    n = q.pop()
         *    if idx < nums.size():
         *       n.left = Node(nums[idx])
         *       q.push(n.left)
         *    idx += 1
         *    if idx < nums.size():
         *       n.right = Node(nums[idx])
         *       q.push(n.right)
         *    idx += 1
         *
         *
         */
        if (nums.isEmpty()) {
            return this;
        }
        // build tree from array
        TreeNode root = new TreeNode(nums.get(0));
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (i < nums.size()) {
            TreeNode n = q.poll();
            assert n != null;
            Integer leftNum = nums.get(i);
            if (leftNum != null) {
                n.left = new TreeNode(nums.get(i));
                q.offer(n.left);
            }
            i += 1;
            if (i >= nums.size()) {
                break;
            }
            Integer rightNum = nums.get(i);
            if (rightNum != null) {
                n.right = new TreeNode(nums.get(i));
                q.offer(n.right);
            }
            i += 1;
        }
        this.root = root;
        return this;
    }

    public static List<Integer> into(BinaryTree tree) {
        if (tree.root == null) {
            return new ArrayList<>();
        }

        Queue<TreeNode> q = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        q.offer(tree.root);

        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            result.add(n.val);
            if (n.left != null) {
                q.offer(n.left);
            }
            if (n.right != null) {
                q.offer(n.right);
            }
        }

        return result;
    }

    public int add(int a, int b) {
        return a + b;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
