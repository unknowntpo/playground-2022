use std::fmt;

use extends_trait::Test;

trait TestExt {
    fn custom_method(&self);
}

impl TestExt for Test {
    fn custom_method(&self) {
        println!("calling custom method for Test")
    }
}

fn main() {
    let t = Test { f1: 3 };
    t.custom_method();
}
