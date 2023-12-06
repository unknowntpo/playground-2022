mod error;
mod one;

fn main() {
    match error::get_super_error() {
        Err(e) => {
            println!("Error: {e}");
        }
        _ => println!("No error"),
    }
}
