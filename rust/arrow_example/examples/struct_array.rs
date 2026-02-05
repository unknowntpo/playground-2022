//! # Struct Array Layout
//!
//! A StructArray is a composite type that groups child arrays.
//! Each child array represents a "field" (like columns in a row).
//! The parent has its own validity bitmap that can mask entire rows.
//!
//! Reference: https://arrow.apache.org/docs/format/Columnar.html#struct-layout

use std::sync::Arc;

use arrow::array::{Array, ArrayRef, Int32Array, StringArray, StructArray};
use arrow::buffer::NullBuffer;
use arrow::datatypes::{DataType, Field, Fields};
use arrow_example::{print_struct_layout, print_validity_bitmap, separator};

fn main() {
    separator("Struct Array: Nested Layout");

    // ── Build child arrays ──────────────────────────────────────────
    let names = StringArray::from(vec![
        Some("Alice"),
        Some("Bob"),
        Some(""), // parent will be null, but child has a value
        Some("Diana"),
        None, // child null
    ]);

    let ages = Int32Array::from(vec![
        Some(30),
        Some(25),
        Some(0), // parent will be null, but child has a value
        None,    // child null
        Some(40),
    ]);

    // ── Define struct fields ────────────────────────────────────────
    let fields = Fields::from(vec![
        Field::new("name", DataType::Utf8, true),
        Field::new("age", DataType::Int32, true),
    ]);

    // Parent validity: row 2 is null at struct level
    let parent_nulls = NullBuffer::from(vec![true, true, false, true, true]);

    let struct_array = StructArray::new(
        fields,
        vec![Arc::new(names) as ArrayRef, Arc::new(ages) as ArrayRef],
        Some(parent_nulls),
    );

    // ── Show logical values ─────────────────────────────────────────
    println!("Logical view (5 rows, 2 fields):");
    println!();
    println!("  Row │ name      │ age  │ struct valid?");
    println!("  ────┼───────────┼──────┼──────────────");
    println!("    0 │ \"Alice\"   │   30 │ yes");
    println!("    1 │ \"Bob\"     │   25 │ yes");
    println!("    2 │ \"\"        │    0 │ NO (parent null)");
    println!("    3 │ \"Diana\"   │ null │ yes");
    println!("    4 │ null      │   40 │ yes");
    println!();
    println!("Array length: {}", struct_array.len());
    println!("Struct null count: {}", struct_array.null_count());

    // ── Layout Overview ─────────────────────────────────────────────
    separator("Physical Layout: Tree of Arrays");

    println!("A StructArray does NOT store its own data buffer.");
    println!("It only has:");
    println!("  1. A validity bitmap (which rows are non-null at struct level)");
    println!("  2. References to child arrays (one per field)");
    println!();
    println!("Conceptual tree:");
    println!();
    println!("  StructArray (parent validity bitmap)");
    println!("  ├── Child 0: StringArray \"name\"");
    println!("  │   ├── validity bitmap");
    println!("  │   ├── offsets buffer");
    println!("  │   └── data buffer");
    println!("  └── Child 1: Int32Array \"age\"");
    println!("      ├── validity bitmap");
    println!("      └── values buffer");

    // ── Parent Validity ─────────────────────────────────────────────
    separator("Parent (Struct) Validity Bitmap");

    print_validity_bitmap(struct_array.nulls(), struct_array.len());

    println!();
    println!("When a struct row is null (e.g., row 2):");
    println!("  → The entire row is considered null");
    println!("  → Child values at that index are IGNORED");
    println!("  → Child arrays may still have non-null values there");
    println!("     (they are physically present but logically masked)");

    // ── Child Details ───────────────────────────────────────────────
    separator("Child Array Details");

    print_struct_layout("root", &struct_array, 0);

    // ── Null Semantics ──────────────────────────────────────────────
    separator("Null Interaction: Parent vs Child");

    println!("Effective validity = parent_valid AND child_valid");
    println!();
    println!("  Row │ parent │  name_child │ name_effective │  age_child │ age_effective");
    println!("  ────┼────────┼─────────────┼────────────────┼────────────┼──────────────");

    for i in 0..struct_array.len() {
        let parent_valid = struct_array.is_valid(i);
        let name_child = struct_array.column(0).is_valid(i);
        let age_child = struct_array.column(1).is_valid(i);
        let name_eff = parent_valid && name_child;
        let age_eff = parent_valid && age_child;

        println!(
            "    {} │ {:>6} │ {:>11} │ {:>14} │ {:>10} │ {:>13}",
            i,
            if parent_valid { "valid" } else { "NULL" },
            if name_child { "valid" } else { "NULL" },
            if name_eff { "VALID" } else { "NULL" },
            if age_child { "valid" } else { "NULL" },
            if age_eff { "VALID" } else { "NULL" },
        );
    }

    // ── Key Takeaways ───────────────────────────────────────────────
    separator("Key Takeaways");

    println!("1. StructArray = validity bitmap + ordered list of child arrays");
    println!("2. No data buffer of its own → zero-copy composition");
    println!("3. Parent null masks entire row regardless of child values");
    println!("4. Effective null = NOT(parent_valid AND child_valid)");
    println!("5. All child arrays must have the same length as the parent");
    println!("6. Struct nesting is recursive: children can be structs too");
    println!();
    println!("Ref: https://arrow.apache.org/docs/format/Columnar.html#struct-layout");
}
