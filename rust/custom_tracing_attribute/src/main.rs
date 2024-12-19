struct A {}

impl A {
    #[custom_tracing_attribute]
    fn method0(a: i32, b: i32) -> i32 {
        a + b
    }
}

fn main() {
    println!("Hello, world!");
}
