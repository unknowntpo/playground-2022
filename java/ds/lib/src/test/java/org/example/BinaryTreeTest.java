package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {
    @Test
    void shouldBuildTreeAndShowTree() {
        Integer[] vals = {3, 5, 7, null, null, 9, 11};
        BinaryTree<Integer> tree = BinaryTree.createBinaryTree(vals);
        List<Integer> actual = new ArrayList<>();
        tree.preOrderTraversal(tree, (node) -> actual.add(node.getVal()));
        assertEquals(Arrays.asList(3, 5, 7, 9, 11), actual);
    }

    @Test
    void shouldPreOrderTraversal() {
        /*
               3
              5  7
            4  6 9 11
         */
        Integer[] vals = {3, 5, 7, 4, 6, 9, 11};
        BinaryTree<Integer> tree = BinaryTree.createBinaryTree(vals);
        List<Integer> actual = new ArrayList<>();
        tree.preOrderTraversal(tree, (node) -> actual.add(node.getVal()));
        assertEquals(Arrays.asList(3, 5, 4, 6, 7, 9, 11), actual);
    }

    @Test
    void shouldPreOrderTraversalUniversal() {
        Integer[] vals = {3, 5, 7, null, null, 9, 11};
        BinaryTree<Integer> tree = BinaryTree.createBinaryTree(vals);
        List<Integer> actual = new ArrayList<>();
        tree.preOrderIterUniversalTraversal(tree, (node) -> actual.add(node.getVal()));
        assertEquals(Arrays.asList(3, 5, 7, 9, 11), actual);
    }
}
