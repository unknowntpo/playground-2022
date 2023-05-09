// https://leetcode.com/problems/combinations/
//
//
fn replace_space(s: String) -> String {
    // Phase 1: append sufficient space
    // new_length = s.len() + num_of_white_space * 2

    let num_of_space = count_white_space(s);
    let new_str_len = s.len() + (num_of_space as usize) * 2;

    let new_vec = Vec::with_capacity(new_str_len).extend_from_slice(&s.into_bytes());

    // Phase 2: replace space from back to front

    // left points to the last element of old string
    let left = s.len() - 1;

    // right points to the last element of string
    let right = new_str_len - 1;
    /*
    while left < right {
        if new_vec[right] ;


    }
    */
}

fn count_white_space(s: String) -> i32 {
    let mut cnt = 0;
    for c in s.chars() {
        if c == ' ' {
            cnt += 1;
        }
    }
    cnt
}

#[cfg(test)]
mod replace_space {
    use super::*;

    #[test]
    fn test_replace_space() {
        let test_cases = [(
            String::from("We are happy."),
            String::from("We%20are%20happy."),
        )];
        for (input, expected) in test_cases {
            let result = replace_space(input);
            assert_eq!(result, expected);
        }
    }
}
