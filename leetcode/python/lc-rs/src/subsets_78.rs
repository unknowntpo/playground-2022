struct Solution;

impl Solution {
    pub fn subsets(nums: Vec<i32>) -> Vec<Vec<i32>> {
        // let res = vec![];
        return vec![];
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    struct tcase {
        name: String,
        nums: Vec<i32>,
        want: Vec<Vec<i32>>,
    }

    fn do_vecs_match<T: PartialEq>(a: &Vec<T>, b: &Vec<T>) -> bool {
        println!("{} {}", a.len(), b.len());
        if a.len() != b.len() {
            return false;
        }
        let matching = a.iter().zip(b.iter()).filter(|&(a, b)| a == b).count();
        matching == a.len() && matching == b.len()
    }

    #[test]
    fn test_subsets() {
        let tcs = vec![tcase {
            name: "example1".to_string(),
            nums: vec![1, 2, 3],
            want: vec![
                vec![],
                vec![1],
                vec![2],
                vec![1, 2],
                vec![3],
                vec![1, 3],
                vec![2, 3],
                vec![1, 2, 3],
            ],
        }];
        for mut tc in tcs {
            // sort
            let mut got = Solution::subsets(tc.nums);
            got.sort();
            tc.want.sort();
            let want = tc.want;

            println!("got: {:?}", got);
            println!("want: {:?}", want);

            // assert_eq!(got.len(), want.len());
            assert_eq!(do_vecs_match(&got, &want), true);

            // for (outer_got, outer_want) in got.iter().zip(want.iter()) {
            //     for (inner_got, inner_want) in outer_got.iter().zip(outer_want.iter()) {
            //         assert_eq!(inner_got, inner_want);
            //     }
            // }
        }
    }
}
