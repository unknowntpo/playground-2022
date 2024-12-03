use anyhow::Result;
use serde::Deserialize;

#[derive(Deserialize, Debug)]
struct Commit {
    sha: String,
    node_id: String,
    commit: CommitInfo,
}

#[derive(Deserialize, Debug)]
struct CommitInfo {
    author: Author,
}

#[derive(Deserialize, Debug)]
struct Author {
    name: String,
    email: String,
    date: String,
}

pub async fn do_work() -> Result<()> {
    println!("Helo");

    let url = "https://api.github.com/repos/apache/gravitino/commits";

    let client = reqwest::Client::new();
    let response = client
        .get(url)
        .header("User-Agent", "rust-github-api-demo")
        .send()
        .await?;

    if response.status().is_success() {
        // FIXME: error handling
        let commits: Vec<Commit> = response.json().await?;

        for c in commits {
            println!("Commit: {:?}", c.commit.author.name);
        }
    }

    Ok(())
}
