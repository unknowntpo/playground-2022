//! # Null (Validity) Buffer Deep Dive
//!
//! Arrow uses a compact bitmap where each bit indicates whether the
//! corresponding array element is valid (1) or null (0).
//! Bits are packed LSB-first (least significant bit first).
//!
//! Reference: https://arrow.apache.org/docs/format/Columnar.html#validity-bitmaps

use arrow::array::{Array, Int32Array};
use arrow_example::{format_bits, print_validity_bitmap, separator};

fn main() {
    separator("Null Buffer: Validity Bitmap Deep Dive");

    // ── Build array with a specific null pattern ────────────────────
    // 10 elements: mix of valid and null to span 2 bytes
    let values: Vec<Option<i32>> = vec![
        Some(10),   // [0] valid
        None,       // [1] null
        Some(30),   // [2] valid
        Some(40),   // [3] valid
        Some(50),   // [4] valid
        None,       // [5] null
        None,       // [6] null
        Some(80),   // [7] valid
        Some(90),   // [8] valid
        None,       // [9] null
    ];
    let array = Int32Array::from(values.clone());

    println!("Logical values ({} elements):", array.len());
    for (i, v) in values.iter().enumerate() {
        match v {
            Some(val) => println!("  [{}] = {}", i, val),
            None => println!("  [{}] = NULL", i),
        }
    }
    println!();
    println!("Null count: {}", array.null_count());

    // ── Bitmap Concept ──────────────────────────────────────────────
    separator("Bitmap Concept: 1 Bit Per Element");

    println!("Arrow packs validity into bytes, LSB-first:");
    println!();
    println!("  Byte 0 holds elements [0..7]:");
    println!("  ┌─────────────────────────────┐");
    println!("  │  bit 7  6  5  4  3  2  1  0 │  ← bit positions in the byte");
    println!("  │  el. 7  6  5  4  3  2  1  0 │  ← corresponding element indices");
    println!("  └─────────────────────────────┘");
    println!();
    println!("  Byte 1 holds elements [8..15]:");
    println!("  ┌─────────────────────────────┐");
    println!("  │  bit 7  6  5  4  3  2  1  0 │");
    println!("  │  el. 15 14 13 12 11 10 9  8 │");
    println!("  └─────────────────────────────┘");
    println!();
    println!("  1 = valid, 0 = null");
    println!("  Unused trailing bits (if len not multiple of 8) are set to 0.");

    // ── Actual Bitmap ───────────────────────────────────────────────
    separator("Actual Bitmap Analysis");

    print_validity_bitmap(array.nulls(), array.len());

    // ── Manual Verification ─────────────────────────────────────────
    separator("Manual Bit Extraction");

    if let Some(nulls) = array.nulls() {
        let bytes = nulls.inner().inner().as_slice();
        println!("Raw bitmap bytes: [{}]",
            bytes.iter().map(|b| format_bits(*b)).collect::<Vec<_>>().join(", "));
        println!();
        println!("Step-by-step validity check for each element:");
        println!();

        for i in 0..array.len() {
            let byte_idx = i / 8;
            let bit_idx = i % 8;
            let byte = bytes[byte_idx];
            let bit = (byte >> bit_idx) & 1;
            let valid = bit == 1;

            println!(
                "  element[{}]: byte[{}] >> {} & 1 = {} >> {} & 1 = {} → {}",
                i,
                byte_idx,
                bit_idx,
                format_bits(byte),
                bit_idx,
                bit,
                if valid { "VALID" } else { "NULL" }
            );
        }
    }

    // ── Space Efficiency ────────────────────────────────────────────
    separator("Space Efficiency");

    let num_bytes = (array.len() + 7) / 8;
    println!("{} elements → {} bytes for validity bitmap", array.len(), num_bytes);
    println!("  vs. {} bytes if using 1 byte per element (bool array)", array.len());
    println!("  Savings: {}x more compact", array.len() / num_bytes);
    println!();
    println!("For 1 million elements:");
    println!("  Bitmap:     ~125 KB");
    println!("  Bool array: ~1000 KB");

    // ── Key Takeaways ───────────────────────────────────────────────
    separator("Key Takeaways");

    println!("1. Validity bitmap: 1 bit per element, packed LSB-first");
    println!("2. Formula: valid = (byte[i / 8] >> (i % 8)) & 1");
    println!("3. Byte count = ceil(len / 8) = (len + 7) / 8");
    println!("4. If no nulls, the bitmap may be omitted entirely");
    println!("5. Unused trailing bits in the last byte are 0");
    println!("6. 8x more compact than a boolean array");
    println!();
    println!("Ref: https://arrow.apache.org/docs/format/Columnar.html#validity-bitmaps");
}
