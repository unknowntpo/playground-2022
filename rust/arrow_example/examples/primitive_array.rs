//! # Primitive (Fixed-Width) Array Layout
//!
//! Arrow stores primitive types in contiguous, fixed-width buffers.
//! An Int32Array of N elements uses exactly N * 4 bytes.
//!
//! Reference: https://arrow.apache.org/docs/format/Columnar.html#fixed-size-primitive-layout

use arrow::array::{Array, Int32Array};
use arrow_example::{print_buffer_hex, print_validity_bitmap, separator};

fn main() {
    separator("Primitive Array: Fixed-Width Layout");

    // ── Build an Int32Array ──────────────────────────────────────────
    let values = vec![Some(1), Some(256), None, Some(42), Some(-1)];
    let array = Int32Array::from(values.clone());

    println!("Logical values: {:?}", values);
    println!("Array length:   {}", array.len());
    println!("Null count:     {}", array.null_count());
    println!("Data type:      {:?}", array.data_type());

    // ── Physical Memory Layout ──────────────────────────────────────
    separator("Physical Layout: Values Buffer");

    println!("Each Int32 value occupies exactly 4 bytes (little-endian).");
    println!();
    println!("Conceptual diagram:");
    println!("┌──────────┬──────────┬──────────┬──────────┬──────────┐");
    println!("│ value[0] │ value[1] │ value[2] │ value[3] │ value[4] │");
    println!("│ 4 bytes  │ 4 bytes  │ 4 bytes  │ 4 bytes  │ 4 bytes  │");
    println!("└──────────┴──────────┴──────────┴──────────┴──────────┘");
    println!("  byte 0-3   byte 4-7  byte 8-11  byte 12-15 byte 16-19");
    println!();

    // Show actual bytes
    let values_buffer = array.values().inner();
    print_buffer_hex("Values buffer (4 bytes per cell, little-endian)", values_buffer, 4);

    println!();
    println!("Little-endian example: value 256 = 0x00000100");
    println!("  Stored as bytes: 00 01 00 00  (least significant byte first)");

    // ── Validity Bitmap ─────────────────────────────────────────────
    separator("Validity Bitmap");

    println!("Arrow uses a validity bitmap to track which elements are null.");
    println!("1 bit per element, packed LSB-first into bytes.");
    println!();
    print_validity_bitmap(array.nulls(), array.len());

    // ── Key Takeaways ───────────────────────────────────────────────
    separator("Key Takeaways");

    println!("1. Fixed-width: element i is at byte offset i * sizeof(type)");
    println!("   → O(1) random access, no indirection");
    println!("2. Contiguous memory: values sit side-by-side in one buffer");
    println!("   → Cache-friendly sequential scans");
    println!("3. Null values still occupy space in the values buffer");
    println!("   → The validity bitmap tells you which to ignore");
    println!("4. Little-endian byte order (on most platforms)");
    println!();
    println!("Ref: https://arrow.apache.org/docs/format/Columnar.html#fixed-size-primitive-layout");
}
