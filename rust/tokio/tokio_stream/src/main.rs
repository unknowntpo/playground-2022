use tokio::sync::mpsc;
use tokio_stream::{StreamExt, StreamMap};

#[tokio::main]
async fn main() {
    let (tx, rx) = mpsc::channel(10);
    let stream = tokio_stream::wrappers::ReceiverStream::new(rx);

    let mut map = StreamMap::new();
    map.insert("stream1", stream.clone());
    map.insert("stream2", stream.clone());
}
