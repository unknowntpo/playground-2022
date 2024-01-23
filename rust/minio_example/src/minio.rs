use crate::error::MinioError;

#[derive(Debug)]
pub struct Minio {
    // debug field
    name: String,
}

pub struct Doc {}

impl Minio {
    fn new(name: &str) -> Self {
        Minio {
            name: name.to_string(),
        }
    }
    fn set(path: &str, doc: String) -> Result<Doc, MinioError> {
        Ok(Doc {})
    }
}

mod tests {
    use super::*;
    #[test]
    fn test_add() {
        assert_eq!(1 + 1, 2);
    }
    #[test]
    fn test_set() {
        let m = Minio::new("my_min");
        assert_eq!(m.name, "my_min");
    }
}
