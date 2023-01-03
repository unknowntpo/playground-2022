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

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_map() {
        assert_eq!(map(vec![1, 2, 3, 4], |x| x + 1), vec![2, 3, 4, 5]);
    }
}
