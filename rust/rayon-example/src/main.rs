use rayon::prelude::*;
use std::collections::HashMap;
use std::fs::File;
use std::io::{BufRead, BufReader};

fn main() {
    // List of file paths
    let file_paths = vec!["file1.txt", "file2.txt", "file3.txt"];

    // Parallel map-reduce word count
    let word_counts: HashMap<String, usize> = file_paths
        .par_iter()
        .map(|file_path| {
            let file = File::open(file_path).expect("Failed to open file");
            let reader = BufReader::new(file);

            let mut word_count = HashMap::new();
            for line in reader.lines() {
                let line = line.expect("Failed to read line");
                let words = line.split_whitespace();

                for word in words {
                    *word_count.entry(word.to_string()).or_insert(0) += 1;
                }
            }

            word_count
        })
        .reduce(
            || HashMap::new(),
            |mut acc, word_count| {
                for (word, count) in word_count {
                    *acc.entry(word).or_insert(0) += count;
                }
                acc
            },
        );

    // Print the word counts
    for (word, count) in &word_counts {
        println!("{}: {}", word, count);
    }
}
