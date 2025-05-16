package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class BalancedBinaryTree110Test extends TestCase {
    private final String name;
    private final List<Integer> tree;
    private final boolean want;

    public BalancedBinaryTree110Test(String name, List<Integer> left, boolean want) {
        this.name = name;
        this.tree = left;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"empty", List.of(), true},
                new Object[]{"one", List.of(1), true},
                new Object[]{"two", Arrays.asList(1, 2), true},
                new Object[]{"three", Arrays.asList(1, 2, 3), true},
                new Object[]{"four - balanced", Arrays.asList(1, 2, 3, 4), true},
                new Object[]{"four - unbalanced", Arrays.asList(1, null, 2, null, 3, 4), false},
                new Object[]{"leetcode testcase: root node balanced, but child nodes unbalanced", Arrays.asList(1, 2, 2, 3, null, null, 3, 4, null, null, 4), false}
        );
    }

    @Test
    public void test() {
        BalancedBinaryTree110.Solution solution = new BalancedBinaryTree110.Solution();
        BinaryTree tree = BinaryTree.of(this.tree);
        boolean got = solution.isBalanced(tree.getRoot());
        assertEquals(want, got);
    }
}

