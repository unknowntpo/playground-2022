use github_api::most_active_contributor::*;

#[tokio::main]
async fn main() {
    match do_work() {
        Ok() => println!("OK"),
        Err(err) => println!("something goes wrong {}", err),
    }
}