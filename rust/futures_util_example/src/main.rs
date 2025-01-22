use std::borrow::BorrowMut;

use futures_util::stream::{self, StreamExt};

async fn process_stream_cloned() {
    let vec = vec![3, 2, 1, 4, 5];
    // let stream_factory = || stream::iter(vec);
    // FIXME: FnOnce, FnMut, Fn
    let stream_factory = || stream::iter(vec.clone()).inspect(|&e| println!("Debug Read: {}", e));

    let debug_stream = stream::iter(vec.clone()).inspect(|&e| println!("Debug Read: {}", e));

    let stream2 = debug_stream;

    stream2
        .for_each(|item| async move {
            println!("Second read: {:?}", item);
        })
        .await;
}

async fn process_stream() {
    let vec = vec![3, 2, 1, 4, 5];
    let stream = stream::iter(vec);

    let collected: Vec<_> = stream.collect().await;

    // First read
    let stream1 = stream::iter(collected.clone());
    stream1
        .for_each(|item| async move {
            println!("First read: {}", item);
        })
        .await;

    // Second read
    let stream2 = stream::iter(collected);
    stream2
        .for_each(|item| async move {
            println!("Second read: {}", item);
        })
        .await;
}

#[tokio::main]
async fn main() {
    process_stream_cloned().await
}
