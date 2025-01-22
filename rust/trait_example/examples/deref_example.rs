use std::fmt::Debug;
use std::ops::{Deref, DerefMut};

#[derive(Debug)]
struct DerefExample<T: Debug> {
    val: T,
}

impl<T: Debug> Deref for DerefExample<T> {
    // add code here
    type Target = T;

    fn deref(&self) -> &Self::Target {
        &self.val
    }
}

impl<T: Debug> DerefMut for DerefExample<T> {
    fn deref_mut(&mut self) -> &mut Self::Target {
        println!("self is {:?}", self.val);
        &mut self.val
    }
}

fn main() {
    let mut x = DerefExample { val: 3 };
    println!("{}", *x);
    *x = 4;
    println!("{}", *x);
}
