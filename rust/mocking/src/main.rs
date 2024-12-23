use mockall::automock;

fn main() {
    println!("Hello, world!");
}

#[automock]
trait MyTrait {
    fn foo(&self, x: u32) -> u32;
}

fn call_with_four(x: &dyn MyTrait) -> u32 {
    x.foo(4)
}

// Ref: https://docs.rs/mockall/latest/mockall/#getting-started

#[cfg(test)]
mod test {
    use super::*;
    use mockall::predicate::*;
    use mockall::*;
    #[test]
    fn test_mock() {
        let mut mock = MockMyTrait::new();
        mock.expect_foo()
            .with(predicate::eq(4))
            .times(1)
            .returning(|x| x + 1);
        assert_eq!(5, call_with_four(&mock));
    }
}
