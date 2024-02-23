pub mod list;

pub fn add(left: usize, right: usize) -> usize {
    left + right
}

#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn it_works() {
        let result = add(2, 2);
        assert_eq!(result, 4);
    }

    #[test]
    fn test_list_add() {
        let result = list::list_add(2, 2); // Use the imported list_add function
        assert_eq!(result, 4);
    }
}
