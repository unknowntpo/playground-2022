use anyhow::Result;
use std::net::SocketAddr;
use tokio::io::AsyncWriteExt;
use tokio::{
    io::AsyncReadExt,
    net::{TcpListener, TcpStream},
};
use tracing::*;
use url::Url;

use proxy::proxy::Error;

#[tokio::main]
async fn main() -> Result<()> {
    tracing_subscriber::fmt::init();
    let addr = "localhost:4000";
    let listener = TcpListener::bind(addr).await?;
    loop {
        let (socket, addr) = listener.accept().await?;
        tokio::spawn(async move {
            trace!("got connection");
            process(socket, addr).await.unwrap();
        });
    }
}

// https://proxiesapi.com/articles/how-to-build-a-simple-http-proxy-in-rust-in-just-40-lines
async fn process(mut socket: TcpStream, addr: SocketAddr) -> Result<()> {
    let mut buffer = [0; 1024];
    let n = socket.read(&mut buffer).await?;

    let request = String::from_utf8_lossy(&buffer[..n]);

    let relative_url = request
        .split_whitespace()
        .nth(1)
        .expect("should have 1th element");

    // Construct the full URL
    let full_url = format!("http://{}{}", addr, relative_url);

    trace!("{}", full_url);

    // Parse the full URL
    let parsed_url = Url::parse(&full_url).expect("Failed to parse the URL");

    // let parsed_url = Url::parse(url).expect("Failed to parse the URL");

    if let Some(query) = parsed_url.query() {
        for (_, dest_url) in url::form_urlencoded::parse(query.as_bytes()) {
            let res = reqwest::get(dest_url.into_owned()).await?.bytes().await?;
            info!("{:?}", res);
            socket.write(&res).await?;
        }
    }
    Ok(())
}
