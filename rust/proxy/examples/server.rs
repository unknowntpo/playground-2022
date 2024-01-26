use anyhow::Result;
use std::future::Future;
use std::io::{copy, Error as IOError};
use std::net::SocketAddr;
use tokio::{
    io::AsyncReadExt,
    net::{TcpListener, TcpStream},
};
use tracing::*;

use proxy::proxy::Error;

#[tokio::main]
async fn main() -> Result<()> {
    std::env::set_var("RUST_LOG", "trace");

    let addr = "localhost:4000";
    let listener = TcpListener::bind(addr).await?;
    loop {
        let (socket, addr) = listener.accept().await?;
        tokio::spawn(async move {
            println!("got connection");
            process(socket, addr).await.unwrap();
        });
    }
}

async fn process(mut socket: TcpStream, addr: SocketAddr) -> Result<(), IOError> {
    let mut buffer = [0; 1024];
    let n = socket.read(&mut buffer).await?;

    let request = String::from_utf8_lossy(&buffer[..n]);

    // let url = request.split_whitespace();

    println!("Request: {}", request);

    // let mut res = reqwest::get(url);

    // copy(&mut res, &mut socket)?;

    Ok(())
}

// fn handle_client(mut stream: TcpStream, addr: SocketAddr) -> impl Future<Output = Result<()>> {
//     async move {
//         // ... handle client logic
//         stream.send
//         Ok(())
//     }
// }
