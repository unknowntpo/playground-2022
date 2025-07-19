package CoinChange;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Solution {
    /**
     * Run this file by:
     * <p>
     *  echo -e "4 3\n1 2 3" | OUTPUT_PATH=$(pwd)/output.txt java CoinChange/Solution.java
     * </p>
     */
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<Long> c = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        // Print the number of ways of making change for 'n' units using coins having the values given by 'c'

        long ways = Result.getWays(n, c);

        bufferedWriter.write(String.valueOf(ways));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

class Result {

    /*
     * Complete the 'getWays' function below.
     *
     * The function is expected to return a LONG_INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER n
     *  2. LONG_INTEGER_ARRAY c
     */

    public static long getWays(int n, List<Long> c) {
        if (n == 0) {
            return 1;
        }
         /*
10 4
2 5 3 6

0 2
1 2

2 3
1 2 3
expect: 2

4 3
1 2 3

expect: 4

{1, 1, 1, 1}
{1, 2, 1}
{1, 3}
         */




        return dfs(0, n, c);
    }

    private static long dfs(int idx, int remain, List<Long> c) {
        // return dfs(remain - c.get(i), i, c) + dfs(remain, i + 1, c);

        var dp = new long[c.size() + 1][remain + 1];
        for (int i = 0; i < dp.length; i++) {
            dp[i][0] = 1;
        }

        for (int i = 0; i < dp.length -1; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                if (j < c.get(i)) {
                    dp[i+1][j] = dp[i][j];
                } else {
                    dp[i+1][j] = dp[i+1][(int)(j - c.get(i))] + dp[i][j];
                }
            }
        }

        System.out.println(Arrays.deepToString(dp));

        return dp[dp.length - 1][remain];



        // dfs(i, remain, c)  = dfs(i, remain - c[i], c) + dfs(i+1, remain, c),

        // dfs(remain, i, c)  = dfs(remain - c[i], i, c) + dfs(remain, i+1,

    }


    // private static long dfsRecur(long remain, int i, List<Long> c) {
    //     // i: choose i.
    //     if (remain < 0 || i >= c.size()) {
    //         // no way
    //         return 0;
    //     }
    //     if (remain == 0) {
    //         return 1;
    //     }

    //     return dfs(remain - c.get(i), i, c) + dfs(remain, i + 1, c);

    //     // dfs(remain, i, c)  = dfs(remain - c[i], i, c) + dfs(remain, i+1, c)


    // }

}
