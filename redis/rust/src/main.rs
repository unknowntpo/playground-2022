extern crate redis;
use redis::Commands;

fn fetch_an_integer() -> redis::RedisResult<isize> {
    let client = redis::Client::open("redis://127.0.0.1/")?;
    let mut con = client.get_connection()?;

    let my_key: String = "my_key".to_string();
    let _: () = con.set(&my_key, 42)?;

    con.get(my_key)
}

fn main() {
    println!("Hello, world!");
    println!("{}", fetch_an_integer().unwrap());
}
