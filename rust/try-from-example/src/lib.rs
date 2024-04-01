struct LessThanZero(i32);

impl TryFrom<i32> for LessThanZero {
    type Error = &'static str;

    fn try_from(value: i32) -> Result<Self, Self::Error> {
        if value >= 0 {
            return Err("LessThanZero only accepts values that is less than 0");
        }
        Ok(LessThanZero(value))
    }
}

#[cfg(test)]
mod test {
    use super::*;
    #[test]
    fn test_try_from() {
        let mut value = 1;
        let less: Result<LessThanZero, &str> = value.try_into();
        assert!(less.is_err());

        value = -1;
        let more: Result<LessThanZero, &str> = value.try_into();
        assert!(more.is_ok());
    }
}
