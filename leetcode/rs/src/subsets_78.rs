use rstest::rstest;

struct Solution;

impl Solution {
    pub fn subsets(nums: Vec<i32>) -> Vec<Vec<i32>> {
        let n = nums.len() as i32;

        let mut res: Vec<Vec<i32>> = vec![];
        let mut path: Vec<i32> = vec![];

        fn dfs(i: i32, n: i32, nums: &Vec<i32>, path: &mut Vec<i32>, res: &mut Vec<Vec<i32>>) {
            if i == n {
                res.push(path[..].to_vec());
                return;
            }

            path.push(nums[i as usize]);
            dfs(i + 1, n, nums, path, res);
            path.pop();
            dfs(i + 1, n, nums, path, res);
        };

        dfs(0, n, &nums, &mut path, &mut res);

        return res;
    }

    pub fn subsets2(nums: Vec<i32>) -> Vec<Vec<i32>> {
        let n = nums.len() as i32;

        let mut res: Vec<Vec<i32>> = vec![];
        let mut path: Vec<i32> = vec![];

        fn dfs(i: i32, n: i32, nums: &Vec<i32>, path: &mut Vec<i32>, res: &mut Vec<Vec<i32>>) {
            res.push(path[..].to_vec());
            if i == n {
                return;
            }

            for i in i..n {
                path.push(nums[i as usize]);
                dfs(i + 1, n, nums, path, res);
                path.pop();
            }
        };

        dfs(0, n, &nums, &mut path, &mut res);

        return res;
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[rstest]
    #[case::custom_name(vec![1, 2, 3], vec![
        vec![],
        vec![1],
        vec![2],
        vec![1, 2],
        vec![3],
        vec![1, 3],
        vec![2, 3],
        vec![1, 2, 3],
    ])]
    fn test_subsets(#[case] nums: Vec<i32>, #[case] want: Vec<Vec<i32>>) {
        // let mut got = Solution::subsets(nums);
        let mut got = Solution::subsets2(nums);
        got.sort();
        let mut want = want;
        want.sort();

        assert_eq!(got, want);
    }
}
