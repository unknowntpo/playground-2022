//! # Variable-Length (String) Array Layout
//!
//! Arrow stores variable-length data using two buffers:
//!   - Offsets buffer: (n+1) int32 values marking start/end of each string
//!   - Data buffer: the concatenated raw bytes
//!
//! Reference: https://arrow.apache.org/docs/format/Columnar.html#variable-size-binary-layout

use arrow::array::{Array, StringArray};
use arrow_example::{print_string_layout, print_validity_bitmap, separator};

fn main() {
    separator("String Array: Variable-Length Layout");

    // ── Build a StringArray ─────────────────────────────────────────
    let values: Vec<Option<&str>> = vec![
        Some("hello"),
        Some("hi"),
        None,
        Some("rust"),
    ];
    let array = StringArray::from(values.clone());

    println!("Logical values: {:?}", values);
    println!("Array length:   {}", array.len());
    println!("Null count:     {}", array.null_count());

    // ── Layout Overview ─────────────────────────────────────────────
    separator("Physical Layout Overview");

    println!("A StringArray (Utf8) consists of 3 buffers:");
    println!();
    println!("  ┌─────────────────────────────────────────────┐");
    println!("  │ Buffer 0: Validity bitmap (1 bit/element)   │");
    println!("  ├─────────────────────────────────────────────┤");
    println!("  │ Buffer 1: Offsets (n+1 int32 values)        │");
    println!("  ├─────────────────────────────────────────────┤");
    println!("  │ Buffer 2: Data (concatenated UTF-8 bytes)   │");
    println!("  └─────────────────────────────────────────────┘");
    println!();
    println!("To read string[i]:");
    println!("  start = offsets[i]");
    println!("  end   = offsets[i+1]");
    println!("  bytes = data[start..end]");
    println!("  length = end - start");

    // ── Validity Bitmap ─────────────────────────────────────────────
    separator("Validity Bitmap");
    print_validity_bitmap(array.nulls(), array.len());

    // ── Offsets + Data ──────────────────────────────────────────────
    separator("Offsets & Data Buffers");

    let offsets: Vec<i32> = (0..=array.len())
        .map(|i| array.value_offsets()[i])
        .collect();
    let data_buffer = &array.values();

    print_string_layout(&offsets, data_buffer, &values);

    // ── Diagram ─────────────────────────────────────────────────────
    separator("How Offsets Map to Data");

    println!("offsets:  [0]    [5]    [7]    [7]    [11]");
    println!("           │      │      │      │       │");
    println!("           ▼      ▼      ▼      ▼       ▼");
    println!("data:     h e l l o  h i        r u s t");
    println!("          ╰──────╯  ╰──╯ ╰────╯ ╰──────╯");
    println!("          \"hello\"   \"hi\"  (null)  \"rust\"");
    println!();
    println!("Notice: null string still has offsets[2]==offsets[3]==7");
    println!("  → zero-length range, no data consumed");

    // ── Key Takeaways ───────────────────────────────────────────────
    separator("Key Takeaways");

    println!("1. n+1 offsets for n strings → O(1) length calc: offsets[i+1] - offsets[i]");
    println!("2. Data buffer is densely packed (no padding between strings)");
    println!("3. Null strings have equal consecutive offsets (zero-length range)");
    println!("4. Offsets are sorted: offsets[0] <= offsets[1] <= ... <= offsets[n]");
    println!("5. Total data size = offsets[n] - offsets[0]");
    println!();
    println!("Ref: https://arrow.apache.org/docs/format/Columnar.html#variable-size-binary-layout");
}
