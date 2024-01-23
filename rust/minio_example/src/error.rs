use thiserror::Error;

#[derive(Error, Debug, PartialEq)]
pub enum MinioError {
    #[error("Could not found resource at key: {0}")]
    NotFound(String),
}
