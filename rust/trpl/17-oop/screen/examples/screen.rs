use screen::screen::{CheckBox, Screen, SelectBox};

fn main() {
    let screen = Screen {
        components: vec![
            Box::new(SelectBox {
                width: 3,
                height: 2,
                options: vec![String::from("Hello"), String::from("World")],
            }),
            Box::new(CheckBox { checked: false }),
        ],
    };
    screen.run();
}
