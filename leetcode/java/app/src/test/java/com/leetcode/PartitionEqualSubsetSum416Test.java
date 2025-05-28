package com.leetcode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class PartitionEqualSubsetSum416Test extends TestCase {
    private final String name;
    private final boolean want;
    private final int[] nums;

    public PartitionEqualSubsetSum416Test(String name, int[] nums, boolean want) {
        this.name = name;
        this.nums = nums;
        this.want = want;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testData() {
        return List.of(
                new Object[]{"one - false", new int[]{1}, false},
                new Object[]{"two - true", new int[]{1, 1}, true},
                new Object[]{"three - true", new int[]{1, 2, 3}, true},
                new Object[]{"three - false", new int[]{4, 2, 1}, false},
                new Object[]{"four - true", new int[]{4, 1, 2, 5}, true}
        );
    }

    @Test
    public void test() {
        PartitionEqualSubsetSum416.Solution solution = new PartitionEqualSubsetSum416.Solution();
        Map<String, Function<int[], Boolean>> fns = Map.of(
                "solution::canPartition", solution::canPartition
        );
        fns.forEach((methodName, method) -> {
            System.out.printf("Calling method %s\n", methodName);
            boolean got = method.apply(nums);
            assertEquals(want, got);
        });
    }
}
