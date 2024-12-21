package org.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {
    @Test
    void testInit() {
        TreeNode<Integer> n = new TreeNode<>(3, null, null);
        assertEquals(3, n.getVal());
    }
}
