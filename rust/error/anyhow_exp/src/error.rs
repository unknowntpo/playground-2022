use thiserror::Error;

#[derive(Error, Debug)]
pub enum SuperError {
    #[error("side kick! {0}")]
    SuperErrorSideKick(String),
}

pub fn get_super_error() -> Result<(), SuperError> {
    Err(SuperError::SuperErrorSideKick(
        "why error? Side kick!".to_string(),
    ))
}
