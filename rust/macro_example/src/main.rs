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

macro_rules! hello {
    ($name:expr) => {
        format!("Hello: {}", $name)
    };
}

macro_rules! math_operation {
    (add $a:expr, $b:expr) => {
        $a + $b
    };

    (subtract $a:expr, $b:expr) => {
        $a - $b
    };
}

macro_rules! create_list {
    ($($ele:expr),*) => {
        vec![$(($ele)),*]
    };
}

mod test {
    use super::*;

    #[test]
    fn test_max_macro() {
        assert_eq!(max!(3, 2), 3);
        assert_eq!(max!(2, 2), 2);
        assert_eq!(max!(2, 3), 3);
    }

    #[test]
    fn test_hello_macro() {
        assert_eq!(hello!("Eric"), "Hello: Eric".to_string())
    }

    #[test]
    fn test_math_operations_macro() {
        assert_eq!(math_operation!(add 1, 2), 3);
        assert_eq!(math_operation!(subtract 3, 2), 1);
    }

    #[test]
    fn test_create_list() {
        assert_eq!(create_list!(1, 2, 3), vec![1, 2, 3]);
    }
}
