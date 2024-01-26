mod proxy;

pub fn add(i: i32, j:i32) -> i32 {
    i+j
}

mod tests {
    use super::*;
    #[test]
    fn test_add() {
        assert!(add(1,2) == 3);
    }
}
