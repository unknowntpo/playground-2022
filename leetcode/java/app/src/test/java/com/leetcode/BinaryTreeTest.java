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
    private final List<Integer> want;
    private final List<Integer> nums;

    public BinaryTreeTest(String name, List<Integer> nums, List<Integer> want) {
        this.name = name;
        this.nums = nums;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(
                new Object[][]{
                        {"empty list", List.of(), List.of()},
                        {"list with 1 element", List.of(1), List.of(1)},
                        {"list with multiple elements", List.of(1,2,3), List.of(1,2,3)},
//                        {"list with null elements", List(1,null,3), List.of(1,null,3)},
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
        List<Integer> got = BinaryTree.into(BinaryTree.of(nums));

        assertEquals(want, got);
    }
}
