// Ref: https://doc.rust-lang.org/rust-by-example/custom_types/enum/testcase_linked_list.html

enum List {
    Cons(u32, Box<List>),
    Nil,
}

impl List {
    fn new() -> List {
        List::Nil
    }

    fn push_head(self, elem: u32) -> List {
        List::Cons(elem, Box::new(self))
    }

    //      t
    // 1 -> x
    //
    fn push_back(&mut self, elem: u32) {
        match self {
            List::Cons(_, ref mut tail) => tail.push_back(elem),
            List::Nil => *self = List::Cons(elem, Box::new(List::Nil)),
        }
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
        list = list.push_head(1);
        list = list.push_head(2);
        list = list.push_head(3);
        assert_eq!(list.stringify(), "3, 2, 1, Nil".to_string());
    }

    #[test]
    fn push_back() {
        let mut list = List::new();
        assert_eq!(list.len(), 0);
        list.push_back(1);
        list.push_back(2);
        list.push_back(3);
        assert_eq!(list.stringify(), "1, 2, 3, Nil".to_string());
    }
}

pub fn list_add(left: usize, right: usize) -> usize {
    left + right
}
