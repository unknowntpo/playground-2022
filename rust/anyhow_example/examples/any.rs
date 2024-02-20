use anyhow::{anyhow, Context, Result};
use std::fs::File;
use std::io::Read;

fn read_config(filename: &str) -> Result<String> {
    let mut file =
        File::open(filename).context(format!("Failed to open config file '{}'", filename))?;
    let mut contents = String::new();
    file.read_to_string(&mut contents)
        .context("Failed to read config file")?;
    Ok(contents)
}

fn parse_config(contents: &str) -> Result<()> {
    // Here you would have your config parsing logic.
    // For the example, let's simulate a potential error:
    if contents.trim().is_empty() {
        return Err(anyhow!("Config file is empty"));
    }
    Ok(())
}

fn main() -> Result<()> {
    let filename = "config.txt";
    let contents = read_config(filename)?;
    parse_config(&contents)?;

    println!("Configuration loaded successfully!");
    Ok(())
}
