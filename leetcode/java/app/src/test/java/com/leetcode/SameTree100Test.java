package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class SameTree100Test extends TestCase {
    private final String name;
    private final List<Integer> left;
    private final List<Integer> right;
    private final boolean want;

    public SameTree100Test(String name, List<Integer> left, List<Integer> right, boolean want) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"empty", List.of(), List.of(), true},
                new Object[]{"one, not the same", List.of(1), List.of(), false},
                new Object[]{"two, same", Arrays.asList(1, 2), Arrays.asList(1,2), true},
                new Object[]{"two, not the same", Arrays.asList(1, 2), Arrays.asList(1, null, 2), false},
                new Object[]{"three, same", Arrays.asList(1, 2, 3),Arrays.asList(1, 2, 3), true},
                new Object[]{"three, not the same with val different", Arrays.asList(1, null, 3),Arrays.asList(1, null, 2), false},
                new Object[]{"official example, not the same (mirrored tree)", Arrays.asList(1, 2, 3),Arrays.asList(1, 3, 2), false}
        );
    }

    @Test
    public void test() {
        SameTree100.Solution solution = new SameTree100.Solution();
        BinaryTree leftTree = BinaryTree.of(left);
        BinaryTree rightTree = BinaryTree.of(right);
        boolean got = solution.isSameTree(leftTree.getRoot(), rightTree.getRoot());
        assertEquals(want, got);
    }
}

