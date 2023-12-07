pub fn find_target_sum_ways(nums: Vec<i32>, target: i32) -> i32 {
    let mut p = target + nums.iter().sum::<i32>() as i32;
      if p < 0 || p % 2 != 0 {
        return 0;
      }      
      p /= 2;
      let n = nums.len();
          // let mut dfs: Vec<Vec<i32>> = vec![vec![0; c + 1]; n];
      println!("{}",p);
      let mut dfs: Vec<Vec<i32>> = vec![vec![0; (p as usize)+1]; n + 1];

      for xs in dfs.iter() {
        println!("{:?}", xs);
      }

      dfs[0][0] = 1;
      for (i, x) in nums.iter().enumerate() {
        for c in 0..p as usize+1{
          if (c as i32) < *x {
            dfs[i+1][c] = dfs[i][c];
          } else{
            dfs[i+1][c] = dfs[i][c] + dfs[i][c-*x as usize];
          }
        }
      }
      return dfs[n][p as usize];
}

#[cfg(test)]
mod target_sum {
    use super::*;
    #[test]
    fn test_target_sum() {
        let test_cases = [
            (vec![1,1,1,1,1], 3, 5),
            (vec![1], 1, 1),
        ];
        for (nums, target, expected) in test_cases {
            let result = target_sum::find_target_sum_ways(nums, target);
            assert_eq!(result, expected);
        }
    }
}
