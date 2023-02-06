#[derive(Debug)]
struct Point {
    x: i32,
    y: i32,
}

#[test]
fn test_point_debug_derive() {
    let p = Point { x: 1, y: 2 };
    assert_eq!(format!("{:?}", p), "Point { x: 1, y: 2 }");
}
