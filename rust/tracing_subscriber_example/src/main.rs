use log::{debug, info, trace};

use tracing::instrument;

#[instrument(level = "debug", skip(b), ret, err)]
fn custom_fn(a: i32, b: i32, c: i32) -> Result<i32, String> {
    Ok(3)
}

fn main() {
    tracing_subscriber::fmt::init();

    println!("Hello, world!");

    info!("this is info log: {}", 3);
    debug!("this is debug log: {}", 4);

    trace!("this is trace log: {}", 4);

    info!("{:?}", custom_fn(3, 1, 2));
    info!("{:?}", custom_fn(4, 4, 1));
}

#[cfg(test)]
mod test {
    use log::{debug, info, trace};

    #[test]
    fn test_log() {
        tracing_subscriber::fmt::init();

        println!("Hello, world!");

        info!("this is info log: {}", 3);
        debug!("this is debug log: {}", 4);

        trace!("this is trace log: {}", 4);
    }
}
