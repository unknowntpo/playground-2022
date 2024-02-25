use std::collections::HashMap;
use std::fmt;

pub struct Trie {
    root: Box<Node>,
}

struct Node {
    children: HashMap<char, Box<Node>>,
    val: String,
    is_end: bool,
}

impl Trie {
    pub fn new() -> Trie {
        Trie {
            root: Box::new(Node {
                children: HashMap::new(),
                val: "".to_string(),
                is_end: true,
            }),
        }
    }
    pub fn insert(&mut self, word: &str) {
        let mut cur = &mut self.root;
        for char in word.chars() {
            cur = cur.children.entry(char).or_insert_with(|| {
                Box::new(Node {
                    children: HashMap::new(),
                    val: "".to_string(),
                    is_end: false,
                })
            })
        }
        cur.is_end = true;
        cur.val = word.to_string();
    }
}

impl fmt::Debug for Node {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        f.debug_struct("Node")
            .field("children", &self.children.keys().collect::<Vec<&char>>())
            .field("val", &self.val)
            .field("is_end", &self.is_end)
            .finish()
    }
}

impl fmt::Debug for Trie {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        f.debug_struct("Trie").field("root", &self.root).finish()
    }
}

impl fmt::Display for Trie {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        fn print_node(node: &Node, f: &mut fmt::Formatter<'_>, depth: usize) -> fmt::Result {
            for indentation in 0..depth {
                write!(f, " ")?
            }
            writeln!(f, "<Node val: '{}' is_end: {}>", node.val, node.is_end)?;

            for (char, child_node) in &node.children {
                write!(f, "{} - ", char)?;
                print_node(child_node, f, depth + 1)?;
            }
            Ok(())
        }

        write!(f, "Trie:\n")?;
        print_node(&self.root, f, 0)
    }
}

// impl Node {
//     fn insert(&mut self, word: &str) {
//         let cur = self;
//         for char in word.chars() {
//             self = children.entry(char).or_insert_with(|| {
//                 Box::new(Node::Cons {
//                     children: HashMap::new(),
//                     val: "".to_string(),
//                 })
//             });
//         }
//     }
// }

pub fn add(a: i32, b: i32) -> i32 {
    a + b
}
