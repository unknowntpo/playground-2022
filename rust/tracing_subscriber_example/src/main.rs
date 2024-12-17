use log::{debug, info, trace};

fn main() {
    tracing_subscriber::fmt::init();

    println!("Hello, world!");

    info!("this is info log: {}", 3);
    debug!("this is debug log: {}", 4);

    trace!("this is trace log: {}", 4);
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
