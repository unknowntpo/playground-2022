package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class MaxDepthOfBinaryTree104Test extends TestCase {
    private final String name;
    private final List<Integer> input;
    private final int want;

    public MaxDepthOfBinaryTree104Test(String name, List<Integer> input, int want) {
        this.name = name;
        this.input = input;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"empty", List.of(), 0},
                new Object[]{"one", List.of(1), 1},
                new Object[]{"two", Arrays.asList(1, 2), 2},
                new Object[]{"three", Arrays.asList(1, 2, 3), 2},
                new Object[]{"five", Arrays.asList(1, 2, null, 3, 4), 3},
                new Object[]{"official example 1", Arrays.asList(3, 9, 20, null, null, 15, 7), 3},
                new Object[]{"official example 2", Arrays.asList(1, null, 2), 2}
        );
    }

    @Test
    public void test() {
        MaxDepthOfBinaryTree104.Solution solution = new MaxDepthOfBinaryTree104.Solution();
        BinaryTree tree = BinaryTree.of(input);
//        int got = solution.maxDepth(tree.getRoot());
        int got = solution.maxDepth2(tree.getRoot());
        assertEquals(want, got);
    }
}

