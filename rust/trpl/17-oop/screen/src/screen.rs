use std::fmt::Display;

pub trait Draw {
    fn draw(&self);
}

pub struct Screen {
    pub components: Vec<Box<dyn Draw>>,
}

impl Screen {
    pub fn run(&self) {
        for c in self.components.iter() {
            c.draw()
        }
    }
}

#[derive(Debug)]
pub struct SelectBox {
    pub width: i32,
    pub height: i32,
    pub options: Vec<String>,
}

impl Display for SelectBox {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(
            f,
            "SelectBox: width: {} height: {}, options: {}",
            self.width,
            self.height,
            self.options.join(" ")
        )
    }
}

impl Draw for SelectBox {
    fn draw(&self) {
        println!("{}", self);
    }
}

pub struct CheckBox {
    pub checked: bool,
}

impl Display for CheckBox {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "CheckBox: checked: {}", self.checked)
    }
}

impl Draw for CheckBox {
    fn draw(&self) {
        println!("{}", self);
    }
}
