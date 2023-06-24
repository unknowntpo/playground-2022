struct Stack<T> {
    items: Vec<T>,
}

impl<T> Stack<T> {
    fn new() -> Stack<T> {
        Stack { items: Vec::new() }
    }

    fn push(&mut self, item: T) {
        self.items.push(item);
    }

    fn pop(&mut self) -> Option<T> {
        self.items.pop()
    }

    fn is_empty(&self) -> bool {
        self.items.is_empty()
    }

    fn size(&self) -> usize {
        self.items.len()
    }

    fn peek(&self) -> Option<&T> {
        self.items.last()
    }
}

fn main() {
    let mut stack = Stack::new();

    stack.push(1);
    stack.push(2);
    stack.push(3);

    println!("Stack size: {}", stack.size());

    while let Some(item) = stack.pop() {
        println!("Popped item: {}", item);
    }

    println!("Is stack empty? {}", stack.is_empty());
}
