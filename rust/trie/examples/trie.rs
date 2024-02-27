use trie::trie::{add, Trie};
fn main() {
    println!("add (1, 2) = {}", add(1, 2));

    let mut t = Trie::new();
    t.insert("hello");
    t.insert("help");
    t.insert("world");
    t.insert("wonder");

    println!("{}", t);

    println!("Contains 'hello': {}", t.search("hello"));
    println!("Contains 'heal': {}", t.search("heal"));

    // // Prefix Search
    // println!("Contains 'hel': {}", t.get_suggestions("hello"));
    // // vec!["world", "wonder"]
    // println!("Contains 'wo': {}", t.get_suggestions("wo"));
}
