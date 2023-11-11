pub struct Greeter {}
impl Greeter {
    pub fn new() -> Greeter {
        Greeter {}
    }

    pub fn greet(&self, name: &str) -> String {
        format!("Hello, {}", name)
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn test_greet() {
        let g = Greeter::new();
        let result = g.greet("world");
        assert_eq!(result, "Hello, world");
    }
}
