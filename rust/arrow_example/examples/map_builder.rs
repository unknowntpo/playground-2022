use arrow::array::{Int32Array, Int32Builder, MapBuilder, StringArray, StringBuilder};

fn main() {
    let string_builder = StringBuilder::new();
    let int_builder = Int32Builder::new();
    // Construct `[{"joe": 1}, {"blogs": 2, "foo": 4}, {}, null]`
    let mut builder = MapBuilder::new(None, string_builder, int_builder);

    // joe:1
    builder.keys().append_value("joe");
    builder.values().append_value(1);
    builder.append(true).unwrap();

    // blogs:2, foo:4
    builder.keys().append_value("blogs");
    builder.values().append_value(2);
    builder.keys().append_value("foo");
    builder.values().append_value(4);
    builder.append(true).unwrap();

    // empty map
    builder.append(true).unwrap();
    // null
    builder.append(false).unwrap();

    let array = builder.finish();
    println!("{:?}", array);
    assert_eq!(array.value_offsets(), &[0, 1, 3, 3, 3]);
    assert_eq!(array.values().as_ref(), &Int32Array::from(vec![1, 2, 4]));
    assert_eq!(array.keys().as_ref(), &StringArray::from(vec!["joe", "blogs", "foo"]));
}