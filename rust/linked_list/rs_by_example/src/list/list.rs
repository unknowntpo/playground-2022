// Ref: https://doc.rust-lang.org/rust-by-example/custom_types/enum/testcase_linked_list.html

enum List {
    Cons(u32, Box<List>),
    Nil,
}

impl List {
    fn new() -> List {
        List::Nil
    }

    // Consume a list, and return the same list with a new element at its front
    fn prepend(self, elem: u32) -> List {
        List::Cons(elem, Box::new(self))
    }

    fn len(&self) -> u32 {
        match self {
            List::Cons(_, ref tail) => 1 + tail.len(),
            List::Nil => 0,
        }
    }

    // Return representation of the list as a (heap allocated) string
    fn stringify(&self) -> String {
        match self {
            List::Cons(head, ref tail) => {
                // `format!` is similar to `print!`, but returns a heap
                // allocated string instead of printing to the console
                format!("{}, {}", head, tail.stringify())
            }
            List::Nil => {
                format!("Nil")
            }
        }
    }
}

mod tests {
    use super::*;
    #[test]
    fn basic_operation() {
        let mut list = List::new();
        assert_eq!(list.len(), 0);
        list = list.prepend(1);
        list = list.prepend(2);
        list = list.prepend(3);
        assert_eq!(list.stringify(), "3, 2, 1, Nil".to_string());
    }
}

pub fn list_add(left: usize, right: usize) -> usize {
    left + right
}
