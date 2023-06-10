// https://sharegpt.com/c/dJ4K7NY

fn divide(x: i32, y: i32) -> Result<i32, String>{
    if y == 0 {
        return Err("divided by zero".to_string())
    }
    Ok(x / y)
}

fn main()  {
    match  divide(1, 0) {
        Ok(value) => println!("got result {}", value),
        Err(error) => println!("got error {}", error),
    }
    match  divide(1, 3) {
        Ok(value) => println!("got result {}", value),
        Err(error) => println!("got error {}", error),
    }
}

