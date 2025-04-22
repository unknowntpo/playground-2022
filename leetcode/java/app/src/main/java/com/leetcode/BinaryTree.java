package com.leetcode;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BinaryTree {
    public static BinaryTree of(List<Integer> nums) {
        return new BinaryTree();
    }

    public static List<Integer> into(BinaryTree tree) {
        return new ArrayList<>();
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
