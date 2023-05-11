// https://leetcode.com/problems/combinations/
//
//
fn replace_space(s: String) -> String {
    // Phase 1: append sufficient space
    // new_length = s.len() + num_of_white_space * 2
    //
    if s.len() == 0 {
        return s;
    }

    let num_of_space = count_white_space(&s);
    let new_str_len = s.len() + (num_of_space as usize) * 2;

    let mut new_vec: Vec<char> = Vec::with_capacity(new_str_len);

    new_vec.extend(s.chars());

    // fill with white space
    for _ in new_vec.len()..new_vec.capacity() {
        new_vec.push(' ');
    }

    new_vec.resize(new_str_len, ' ');

    // Phase 2: replace space from back to front

    // left points to the last element of old string
    let mut left = s.len();

    // right points to the last element of string
    let mut right = new_str_len;
    while left < right {
        left -= 1;
        right -= 1;
        if new_vec[left] == ' ' {
            new_vec[right] = '0';
            new_vec[right - 1] = '2';
            new_vec[right - 2] = '%';
            right -= 2;
        } else {
            new_vec[right] = new_vec[left];
        }
    }

    let out: String = new_vec.into_iter().collect();
    out
}

// [_]
// [___]
//  l  r
// [___]
// [___]

fn count_white_space(s: &str) -> i32 {
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
        let test_cases = [
            (
                String::from("We are happy."),
                String::from("We%20are%20happy."),
            ),
            (String::from("a b c "), String::from("a%20b%20c%20")),
            (String::from(" "), String::from("%20")),
            //(String::from(""), String::from("")),
        ];
        for (input, expected) in test_cases {
            let result = replace_space(input);
            assert_eq!(result, expected);
        }
    }

    #[test]
    fn test_count_white_space() {
        let test_cases = [
            (String::from("We are happy."), 2),
            (String::from("a b c d. "), 4),
        ];
        for (input, expected) in test_cases {
            let result = count_white_space(&input);
            assert_eq!(result, expected);
        }
    }
}
