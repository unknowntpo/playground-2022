use std::collections::HashMap;

fn main() {
    let mut m = HashMap::new();
    m.insert("ABC", 1);
    m.insert("DEF", 2);
    println!("contain keys? {:?}", m.contains_key("ABC"));

    let idx = ["ABC", "DEF", "NOTFOUND"];
    for &key in &idx {
        match m.get(key) {
            Some(v) => println!("{key}: {v}"),
            None => println!("{key} doesn't exist"),
        }
    }
}
