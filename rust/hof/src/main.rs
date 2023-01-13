fn main() {
    println!("Hello, world!");
}

pub fn map<T: Clone>(arr: Vec<T>, f: fn(e: T) -> T) -> Vec<T> {
    let mut out = Vec::with_capacity(arr.len());

    for e in arr.iter() {
        // out.push(1);
        out.push(f(e.clone()));
    }

    return out;
}

pub fn filter<T: Clone>(arr: Vec<T>, f: fn(e: T) -> bool) -> Vec<T> {
    let mut out = Vec::with_capacity(arr.len());

    for e in arr.iter() {
        if f(e.clone()) {
            out.push(e.clone())
        }
    }

    return out;
}

// Ref:
// https://doc.rust-lang.org/book/ch19-05-advanced-functions-and-closures.html#returning-closures
pub fn add(x: i32) -> Box<dyn Fn(i32) -> i32> {
    Box::new(|y| y + 1)
}

// pub fn add(x: i32) -> Box<dyn Fn(i32) -> i32> {
//     Box::new(|y| x + y);
// }

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_filter() {
        assert_eq!(filter(vec![1, 2, 3, 4], |x| x % 2 == 0), vec![2, 4]);
    }

    #[test]
    fn test_map() {
        assert_eq!(map(vec![1, 2, 3, 4], |x| x + 1), vec![2, 3, 4, 5]);
    }

    #[test]
    fn test_add() {
        assert_eq!(add(1)(2), 3)
    }
}
