use learn_rust_with_entirely_too_many_linked_lists::first::{Link, List};

fn main() {
    let list: List = List {
        head: Link::More(Box::new(Node {
            elem: 3,
            next: Link::Empty,
        })),
    };
    match list.head {
        Link::Empty => {}
        Link::More(node) => {
            println!("elem: {:?}", node.elem);
        }
    }
}
