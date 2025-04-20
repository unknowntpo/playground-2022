package com.leetcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BinaryTreeTest {
    private final String name;
    private final int want;
    private final int a;
    private final int b;

    public BinaryTreeTest(String name, int a, int b, int want) {
        this.name = name;
        this.want = want;
        this.a = a;
        this.b = b;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(
                new Object[][]{
                        {"case0", 1, 3, 6},
                        {"case1", 1, 2, 3},
                        {"case2", 1, 2, 3}
                }
        );
    }

    @Test
    public void testAdd() {
        BinaryTree binaryTree = new BinaryTree();
        assertEquals(want, binaryTree.add(a, b));
    }
}
