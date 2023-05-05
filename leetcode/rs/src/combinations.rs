// https://leetcode.com/problems/combinations/

#[allow(dead_code)]
fn combine(n: i32, k: i32) -> Vec<Vec<i32>> {
    // Your implementation here
    //return vec![vec![n, k]];
    let mut out: Vec<Vec<i32>> = Vec::new();
    let acc = vec![];
    dp(1, n, k, acc, &mut out);

    return out;
}

/*

[1 2 3 4]

[1] - [1, 2], [1, 3], [1, 4]
[2] - [2, 3], [2, 4]
[3] - [3, 4]

*/

// dp(n, k, acc)

fn dp(start: i32, end: i32, expect_len: i32, acc: Vec<i32>, out: &mut Vec<Vec<i32>>) {
    // if len(acc) == expectLen
    // // append out then return
    // for i from start to end;
    // // v := acc.append(i)
    // // dp(i, end, v)

    if acc.len() == (expect_len as usize) {
        out.push(acc);
        return;
    }

    for i in start..=end {
        let mut v = acc.clone();
        v.push(i);
        dp(i + 1, end, expect_len, v, out);
    }
}

#[cfg(test)]
mod combinations {
    use super::*;

    #[test]
    fn test_combine() {
        let test_cases = [
            (
                4,
                2,
                vec![
                    vec![1, 2],
                    vec![1, 3],
                    vec![1, 4],
                    vec![2, 3],
                    vec![2, 4],
                    vec![3, 4],
                ],
            ),
            (1, 1, vec![vec![1]]),
            (
                5,
                3,
                vec![
                    vec![1, 2, 3],
                    vec![1, 2, 4],
                    vec![1, 2, 5],
                    vec![1, 3, 4],
                    vec![1, 3, 5],
                    vec![1, 4, 5],
                    vec![2, 3, 4],
                    vec![2, 3, 5],
                    vec![2, 4, 5],
                    vec![3, 4, 5],
                ],
            ),
        ];
        for (n, k, expected) in test_cases {
            let result = combine(n, k);
            assert_eq!(result, expected);
        }
    }
}
