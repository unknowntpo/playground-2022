package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class BinaryTreeRightSideView199Test extends TestCase {
    private final String name;
    private final List<Integer> tree;
    private final List<Integer> want;

    public BinaryTreeRightSideView199Test(String name, List<Integer> left, List<Integer> want) {
        this.name = name;
        this.tree = left;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"empty", List.of(), List.of()},
                new Object[]{"one", List.of(1), List.of(1)},
                new Object[]{"two", Arrays.asList(1, 2), List.of(1, 2)},
                new Object[]{"three", Arrays.asList(1, 2, 3), List.of(1, 3)},
                new Object[]{"three - has null", Arrays.asList(1, 2, null, 3), List.of(1, 2, 3)},
                new Object[]{"leetcode example 1", Arrays.asList(1, 2, 3, null, 5, null, 4), List.of(1, 3, 4)},
                new Object[]{"leetcode example 2", Arrays.asList(1, 3, 4, null, null, null, 5), List.of(1, 4, 5)}
        );
    }

    @Test
    public void test() {
        BinaryTreeRightSideView199.Solution solution = new BinaryTreeRightSideView199.Solution();
        Map<String, Function<BinaryTree.TreeNode, List<Integer>>> fns = Map.of(
                "solution::rightSideView", solution::rightSideView);
        BinaryTree tree = BinaryTree.of(this.tree);
        fns.forEach((methodName, method) -> {
            System.out.printf("Calling method %s\n", methodName);
            List<Integer> got = method.apply(tree.getRoot());
            assertEquals(want, got);
        });
    }
}
