use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize)]
struct Employee {
    id: String,
    employee_name: String,
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
    let json_data = r#"
    {
        "status": "success",
        "data": [
            {
                "id": "1",
                "employee_name": "Tiger Nixon",
                "employee_salary": "320800",
                "employee_age": "61",
                "profile_image": ""
            }
        ]
    }
    "#;
    match serde_json::from_str::<Resp>(json_data) {
        Ok(r) => {
            println!("Status: {}", r.status);
            for employee in r.data {
                println!("Employee: {:?}", employee);
            }
        }
        Err(e) => println!("Got error: {}", e),
    };
}
