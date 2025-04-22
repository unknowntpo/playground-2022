package com.leetcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class BinaryTreeTest {
    private final String name;
    private final int[] want;
    private final int[] nums;

    public BinaryTreeTest(String name, int[] nums, int[] want) {
        this.name = name;
        this.nums = nums;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(
                new Object[][]{
                        {"empty list", new int[]{}, new int[]{}},
                        {"list with 1 element", new int[]{1}, new int[]{1}},
                        {"list with multiple elements", new int[]{1, 2, 3}, new int[]{1, 2, 3}},
                }
        );
    }

    //    @Test
//    public void testAdd() {
//        BinaryTree binaryTree = new BinaryTree();
//        assertEquals(want, binaryTree.add(a, b));
//    }
//
    @Test
    public void testBuildTree() {
        List<Integer> got = BinaryTree.into(BinaryTree.of(List.of(3)));

        assertEquals(want, got.toArray(Integer[]::new));
    }
}
