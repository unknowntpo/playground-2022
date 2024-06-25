use std::borrow::Cow;

use serde::{Deserialize, Serialize};
use validator::{Validate, ValidationError};

#[derive(Debug, Serialize, Deserialize, Validate)]
struct Employee {
    id: String,
    employee_name: String,
    #[validate(custom(function=validate_employee_salary))]
    employee_salary: String,
    employee_age: String,
    profile_image: String,
}

#[derive(Debug, Serialize, Deserialize)]
struct Resp {
    status: String,
    data: Vec<Employee>,
}

pub fn parse(json_data: &str) {
    match serde_json::from_str::<Resp>(json_data) {
        Ok(r) => {
            println!("Status: {}", r.status);
            for employee in r.data {
                println!("Employee: {:?}", employee);
                match employee.validate() {
                    Ok(_) => println!("validation PASS"),
                    Err(e) => println!("validation failed: {}", e),
                };
            }
        }
        Err(e) => println!("Got error: {}", e),
    };
}

fn validate_employee_salary(salary: &str) -> Result<(), ValidationError> {
    let salary: f64 = salary.parse().map_err(|_| ValidationError::new("failed"))?;

    if salary > 3000.0 {
        Err(ValidationError::new("Salary exceeds maximum limit"))
    } else {
        Ok(())
    }
}
