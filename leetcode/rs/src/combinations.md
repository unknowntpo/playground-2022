

C42


fn combine(n: i32, k: i32) -> Vec<Vec<i32>> {
    // Your implementation here
    return dp(0, n, k, vec![vec![]])
    return vec![vec![n, k]];
}

fn dp(start, n, k, out) -> Vec<Vec<i32>> {
    // Stop condition
    if (start == n) {return out }

    for (i = start+1; i < n; i++) {
        out = append(out, dp(i, n, k-1, out))
    }
}

dp(0, 4, 2, [[]])
    out = append(out, dp(1,4, 1, [[]]))
        dp(1,4,1)

    out = append(out, dp(3,4, 1, [[]]))
        dp(3,4,1, [[]])
            i = 4

    out = append(out, dp(4,4, 1, [[]]))
        return





req:
- start idx
- k

 s
[1,2,3,4]

[1] -> [1, 2], [1,3], [1,4]

[2] -> [2, 3], [2,4]


[3] -> [3,4]

[4] -> 