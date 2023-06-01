// https://leetcode.com/problems/two-sum/

// Given an array of integers nums and an integer target,
// return indices of the two numbers such that they add up to target.
// You may assume that each input would have exactly one solution,
// and you may not use the same element twice.

// You can return the answer in any order.

use std::collections::HashMap;

fn two_sum(nums: Vec<i32>, target: i32) -> Vec<i32> {
    let mut map: HashMap<i32, i32> = HashMap::new();
    // map from target - v to i
    for (i, v) in nums.iter().enumerate() {
        let diff = target - v;
        if let Some(pairIdx) = map.get(&v) {
            return vec![*pairIdx as i32, i as i32];
        }
        map.insert(diff, i as i32);
    }
    return vec![];
}

#[cfg(test)]
mod two_sum {
    use super::*;
    #[test]
    fn test_two_sum() {
        let test_cases = [
            (vec![2, 7, 11, 15], 9, vec![0, 1]),
            (vec![3, 2, 4], 6, vec![1, 2]),
            (vec![3, 3], 6, vec![0, 1]),
        ];
        for (nums, target, expected) in test_cases {
            let result = two_sum(nums, target);
            assert_eq!(result.clone().sort(), expected.clone().sort());
        }
    }
}
