package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class InvertBinaryTree226Test extends TestCase {
    private final String name;
    private final List<Integer> input;
    private final List<Integer> want;

    public InvertBinaryTree226Test(String name, List<Integer> input, List<Integer> want) {
        this.name = name;
        this.input = input;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"empty", List.of(), List.of()},
                new Object[]{"one", List.of(1), List.of(1)},
                new Object[]{"two", Arrays.asList(1, 2), Arrays.asList(1, null, 2)},
                new Object[]{"three", Arrays.asList(1, 2, 3), Arrays.asList(1, 3, 2)},
                new Object[]{"seven", Arrays.asList(4, 2, 7, 1, 3, 6, 9), Arrays.asList(4, 7, 2, 9, 6, 3, 1)}
        );
    }

    @Test
    public void test() {
        InvertBinaryTree226.Solution solution = new InvertBinaryTree226.Solution();
        BinaryTree tree = BinaryTree.of(input);
        tree.setRoot(solution.invertTree(tree.getRoot()));
        assertEquals(want, tree.into());
    }
}

