use thiserror::Error;

#[derive(Error, Debug, PartialEq)]
pub enum Error {
    #[error("dummy error")]
    Dummy,
}
