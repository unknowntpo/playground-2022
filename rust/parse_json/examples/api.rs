use parse_json::parse;

fn main() {
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

    parse::parse(&json_data);
}
