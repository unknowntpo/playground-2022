// https://sharegpt.com/c/dJ4K7NY

fn divide(dividend: i32, divisor: i32) -> Result<i32, String> {
    if divisor == 0 {
        return Err(String::from("Cannot divide by zero"));
    }
    Ok(dividend / divisor)
}
fn main() {
    let dividend = 10;
    let divisor = 0;

    match divide(dividend, divisor) {
        Ok(value) => println!("Result: {}", value),
        Err(error) => println!("Result: {}", error),
    }
}
