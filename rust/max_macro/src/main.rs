fn main() {
    println!("Hello, world!");
}

macro_rules! max {
    ($x: expr, $y: expr) => {
        if $x > $y {
            $x
        } else {
            $y
        }
    };
}

#[test]
fn test_max_macro() {
    assert_eq!(max!(3, 2), 3);
    assert_eq!(max!(2, 2), 2);
    assert_eq!(max!(2, 3), 3);
}
