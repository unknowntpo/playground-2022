// https://leetcode.com/problems/combinations/

#[allow(dead_code)]
fn combine(n: i32, k: i32) -> Vec<Vec<i32>> {
    // Your implementation here
    return vec![vec![n, k]];
}

fn dp(n: i32, k: i32, start: i32, res: Vec<Vec<i32>>) {
    if start == k {
        return;
    }
    for i in 0..n {
        dp(n, k - 1, i + 1, res)
    }
    return;
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
