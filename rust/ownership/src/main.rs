fn main() {
    let mut s = String::from("hello");
    println!("string owner: {}", s);
    println!("string owner: {}", s);
    // owner(s);
    borrow(&s);
    borrow(&s);
    mut_borrow(&mut s);
    mut_borrow(&mut s);
    borrow(&s);
}

fn owner(s: String) {
    println!("s: {}", s);
}

fn borrow(s: &String) {
    println!("s: {}", s);
}

fn mut_borrow(s: &mut String) {
    s.push_str("world");
    println!("in mut_borrow, s {}", s)
}
