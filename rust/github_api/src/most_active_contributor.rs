use anyhow::Result;
use serde::Deserialize;

#[derive(Deserialize, Debug)]
struct Commit {
    sha: String,
    node_id: String,
    commit: Option<String>,
    url: String,
    html_url: u32,
    comments_url: u32,
    author: String,
}

#[derive(Deserialize, Debug)]
struct Author {
    login: String,
}

pub async fn do_work() -> Result<()> {
    println!("Helo");
    Ok(())
}
