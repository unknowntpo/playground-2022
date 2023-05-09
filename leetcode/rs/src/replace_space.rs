// https://leetcode.com/problems/combinations/
//
//
fn replace_space(s: String) -> String {
    s
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
