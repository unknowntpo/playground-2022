use serde::Deserialize;
use std::error::Error;

#[derive(Deserialize, Debug)]
struct Repository {
    name: String,
    full_name: String,
    description: Option<String>,
    html_url: String,
    stargazers_count: u32,
    forks_count: u32,
    language: Option<String>,
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    let url = "https://api.github.com/users/unknowntpo/repos";

    let client = reqwest::Client::new();
    let response = client
        .get(url)
        .header("User-Agent", "rust-github-api-demo")
        .send()
        .await?;

    if response.status().is_success() {
        let repos: Vec<Repository> = response.json().await?;

        for repo in repos {
            println!("Name: {}", repo.name);
            println!("Full Name: {}", repo.full_name);
            println!(
                "Description: {}",
                repo.description
                    .unwrap_or_else(|| "No description".to_string())
            );
            println!("URL: {}", repo.html_url);
            println!("Stars: {}", repo.stargazers_count);
            println!("Forks: {}", repo.forks_count);
            println!(
                "Language: {}",
                repo.language.unwrap_or_else(|| "Not specified".to_string())
            );
            println!("---");
        }
    } else {
        println!("Error: {}", response.status());
    }

    Ok(())
}
