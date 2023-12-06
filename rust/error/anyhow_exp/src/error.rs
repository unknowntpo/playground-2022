use anyhow::Result;
use std::fs;
use thiserror::Error;

#[derive(Error, Debug)]
pub enum SuperError {
    #[error("side kick! {0}")]
    SuperErrorSideKick(String),
}

pub fn get_super_error() -> Result<()> {
    fs::read_to_string("non-exist")?;
    Err(SuperError::SuperErrorSideKick("why error? Side kick!".to_string()).into())
}
