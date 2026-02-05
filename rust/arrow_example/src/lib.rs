use arrow::array::{Array, AsArray};
use arrow::buffer::{Buffer, NullBuffer};
use arrow::datatypes::DataType;

/// Print a section divider with a title.
pub fn separator(title: &str) {
    println!();
    println!("═══ {} ═══", title);
    println!();
}

/// Format a byte as a binary string like "0b00011101".
pub fn format_bits(byte: u8) -> String {
    format!("0b{:08b}", byte)
}

/// Print a buffer as an ASCII box diagram with hex values.
///
/// `bytes_per_cell` controls how many bytes each cell groups together
/// (e.g., 4 for Int32 values, 1 for raw byte display).
pub fn print_buffer_hex(name: &str, buffer: &Buffer, bytes_per_cell: usize) {
    let data = buffer.as_slice();
    if data.is_empty() {
        println!("{}: (empty)", name);
        return;
    }

    let cells: Vec<String> = data
        .chunks(bytes_per_cell)
        .enumerate()
        .map(|(i, chunk)| {
            let hex: Vec<String> = chunk.iter().map(|b| format!("{:02x}", b)).collect();
            let hex_str = hex.join(" ");
            // Also show the interpreted little-endian integer for 4-byte cells
            if bytes_per_cell == 4 && chunk.len() == 4 {
                let val = i32::from_le_bytes([chunk[0], chunk[1], chunk[2], chunk[3]]);
                format!("[{}]: {} (= {})", i, hex_str, val)
            } else {
                format!("[{}]: {}", i, hex_str)
            }
        })
        .collect();

    println!("{}:", name);
    let top = cells.iter().map(|c| "─".repeat(c.len() + 2)).collect::<Vec<_>>();
    let mid = cells.iter().map(|c| format!(" {} ", c)).collect::<Vec<_>>();
    let bot = cells.iter().map(|c| "─".repeat(c.len() + 2)).collect::<Vec<_>>();

    println!("┌{}┐", top.join("┬"));
    println!("│{}│", mid.join("│"));
    println!("└{}┘", bot.join("┴"));
}

/// Print the validity (null) bitmap with LSB bit analysis.
pub fn print_validity_bitmap(nulls: Option<&NullBuffer>, len: usize) {
    match nulls {
        None => {
            println!("Validity bitmap: absent (all values are valid)");
        }
        Some(null_buffer) => {
            println!("Validity bitmap ({} elements):", len);
            let bytes = null_buffer.inner().inner().as_slice();
            let num_bytes = (len + 7) / 8;

            for byte_idx in 0..num_bytes {
                let byte = bytes[byte_idx];
                println!();
                println!("  Byte {}: {}", byte_idx, format_bits(byte));
                println!("          ││││││││");
                println!("  Bit:    76543210  (LSB numbering: read right → left)");
                println!();

                // Show per-element validity for bits in this byte
                let start = byte_idx * 8;
                let end = std::cmp::min(start + 8, len);
                for i in start..end {
                    let bit = (byte >> (i % 8)) & 1;
                    let status = if bit == 1 { "VALID" } else { "NULL " };
                    println!(
                        "    element[{}] → bit {} = {} → {}",
                        i,
                        i % 8,
                        bit,
                        status
                    );
                }
            }

            println!();
            println!("  Formula: valid = (byte[i/8] >> (i%8)) & 1");
        }
    }
}

/// Print the layout of a string (variable-length) array:
/// offsets buffer + data buffer.
pub fn print_string_layout(
    offsets: &[i32],
    data_buffer: &Buffer,
    values: &[Option<&str>],
) {
    // Offsets
    println!("Offsets buffer ({} entries for {} strings):", offsets.len(), offsets.len() - 1);
    let offset_strs: Vec<String> = offsets.iter().enumerate().map(|(i, o)| format!("[{}]: {}", i, o)).collect();
    let top = offset_strs.iter().map(|s| "─".repeat(s.len() + 2)).collect::<Vec<_>>();
    let mid = offset_strs.iter().map(|s| format!(" {} ", s)).collect::<Vec<_>>();
    let bot = offset_strs.iter().map(|s| "─".repeat(s.len() + 2)).collect::<Vec<_>>();
    println!("┌{}┐", top.join("┬"));
    println!("│{}│", mid.join("│"));
    println!("└{}┘", bot.join("┴"));

    // Derived lengths
    println!();
    println!("String lengths (offsets[i+1] - offsets[i]):");
    for i in 0..offsets.len() - 1 {
        let len = offsets[i + 1] - offsets[i];
        let val = values[i].unwrap_or("(null)");
        println!("  string[{}]: offsets[{}] - offsets[{}] = {} - {} = {} bytes  → {:?}",
            i, i + 1, i, offsets[i + 1], offsets[i], len, val);
    }

    // Data buffer
    println!();
    let data = data_buffer.as_slice();
    let data_str = String::from_utf8_lossy(data);
    println!("Data buffer ({} bytes): {:?}", data.len(), data_str);

    let byte_strs: Vec<String> = data.iter().enumerate().map(|(i, &b)| {
        if b.is_ascii_graphic() || b == b' ' {
            format!("[{}]: '{}' ({:02x})", i, b as char, b)
        } else {
            format!("[{}]: {:02x}", i, b)
        }
    }).collect();

    if !byte_strs.is_empty() {
        let top = byte_strs.iter().map(|s| "─".repeat(s.len() + 2)).collect::<Vec<_>>();
        let mid = byte_strs.iter().map(|s| format!(" {} ", s)).collect::<Vec<_>>();
        let bot = byte_strs.iter().map(|s| "─".repeat(s.len() + 2)).collect::<Vec<_>>();
        println!("┌{}┐", top.join("┬"));
        println!("│{}│", mid.join("│"));
        println!("└{}┘", bot.join("┴"));
    }
}

/// Print the layout of a struct array, recursively showing child arrays.
pub fn print_struct_layout(
    name: &str,
    array: &dyn Array,
    indent: usize,
) {
    let pad = " ".repeat(indent);
    let dt = array.data_type();

    match dt {
        DataType::Struct(fields) => {
            let struct_arr = array.as_struct();
            println!("{}StructArray {:?} ({} rows)", pad, name, array.len());
            println!("{}├── validity: {} nulls", pad, array.null_count());
            if let Some(nulls) = array.nulls() {
                let bytes = nulls.inner().inner().as_slice();
                let num_bytes = (array.len() + 7) / 8;
                for byte_idx in 0..num_bytes {
                    println!("{}│   byte {}: {}", pad, byte_idx, format_bits(bytes[byte_idx]));
                }
            }

            for (i, field) in fields.iter().enumerate() {
                let child = struct_arr.column(i);
                let is_last = i == fields.len() - 1;
                let connector = if is_last { "└" } else { "├" };
                let child_pad = if is_last { "    " } else { "│   " };

                println!(
                    "{}{}── Child {}: {} ({:?})",
                    pad, connector, i, field.name(), field.data_type()
                );
                print_child_array_detail(
                    child.as_ref(),
                    &format!("{}{}", pad, child_pad),
                );
            }
        }
        _ => {
            println!("{}{} ({:?}, {} rows)", pad, name, dt, array.len());
        }
    }
}

fn print_child_array_detail(array: &dyn Array, pad: &str) {
    // Validity
    if let Some(nulls) = array.nulls() {
        let bytes = nulls.inner().inner().as_slice();
        let num_bytes = (array.len() + 7) / 8;
        print!("{}validity: ", pad);
        for byte_idx in 0..num_bytes {
            print!("{} ", format_bits(bytes[byte_idx]));
        }
        println!("({} nulls)", array.null_count());
    } else {
        println!("{}validity: none (all valid)", pad);
    }

    match array.data_type() {
        DataType::Int32 => {
            let arr = array.as_primitive::<arrow::datatypes::Int32Type>();
            print!("{}values: [", pad);
            for i in 0..arr.len() {
                if i > 0 { print!(", "); }
                if arr.is_null(i) {
                    print!("null");
                } else {
                    print!("{}", arr.value(i));
                }
            }
            println!("]");
        }
        DataType::Utf8 => {
            let arr = array.as_string::<i32>();
            let offsets: Vec<i32> = (0..=arr.len()).map(|i| arr.value_offsets()[i]).collect();
            print!("{}offsets: {:?}", pad, offsets);
            println!();
            print!("{}values: [", pad);
            for i in 0..arr.len() {
                if i > 0 { print!(", "); }
                if arr.is_null(i) {
                    print!("null");
                } else {
                    print!("{:?}", arr.value(i));
                }
            }
            println!("]");
        }
        _ => {
            println!("{}(unsupported type for detail display: {:?})", pad, array.data_type());
        }
    }
}
