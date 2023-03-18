// https://leetcode.com/problems/two-sum/

// Given an array of integers nums and an integer target,
// return indices of the two numbers such that they add up to target.
// You may assume that each input would have exactly one solution,
// and you may not use the same element twice.

// You can return the answer in any order.

use std::collections::HashMap;

fn two_sum(nums: Vec<i32>, target: i32) -> Vec<i32> {
    // num -> idx
    // [2,7,11,15]

    // 2 -> 0
    // 7 -> 1

    // for idx, ele in nums
    //     if key: target - ele exist in map:
    //         return [idx, map[key]]
    //     map[ele] = idx
    let mut map: HashMap<i32, i32> = HashMap::new();

    for (i, v) in nums.iter().enumerate() {
        if map.contains_key(&(target - v)) {
            return vec![i, map[key]];
        }
    }
    return vec![1, 2, 3];
}

#[cfg(test)]
mod two_sum {
    use super::*;
    fn test_two_sum() {
        let test_cases = [
            (vec![2, 7, 11, 15], 9, vec![0, 1]),
            (vec![3, 2, 4], 6, vec![1, 2]),
            (vec![3, 3], 6, vec![0, 1]),
        ];
        for (nums, target, expected) in test_cases {
            let result = two_sum(nums, target);
            assert_eq!(result, expected);
        }
    }
}
