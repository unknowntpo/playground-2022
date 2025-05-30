package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class TargetSum494Test extends TestCase {
    private final String name;
    private final int[] nums;
    private final int target;
    private final int want;

    public TargetSum494Test(String name, int[] nums, int target, int want) {
        this.name = name;
        this.nums = nums;
        this.target = target;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"one", new int[]{1}, 1, 1},
                new Object[]{"two numbers", new int[]{1, 1}, 2, 1},
                new Object[]{"three numbers", new int[]{1, 1, 1}, 1, 3},
                new Object[]{"three different numbers", new int[]{1, 3, 2}, 4, 1},
                new Object[]{"three different numbers, negative target", new int[]{1, 3, 2}, -4, 1}
        );
    }

    @Test
    public void test() {
        TargetSum494.Solution solution = new TargetSum494.Solution();
        Map<String, BiFunction<int[], Integer, Integer>> fns = Map.of(
                "solution::findTargetSumWaysOld", solution::findTargetSumWaysOld,
                "solution::findTargetSumWays", solution::findTargetSumWays,
                "solution::findTargetSumWays2DArray", solution::findTargetSumWays2DArray,
                "solution::findTargetSumWays1DArray", solution::findTargetSumWays1DArray
        );
        fns.forEach((methodName, method) -> {
            System.out.printf("Calling method %s\n", methodName);
            int got = method.apply(nums, target);
            assertEquals(want, got);
        });
    }
}
