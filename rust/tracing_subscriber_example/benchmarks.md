

# Compare the performance between using `instrument` attribute and using `debug!` for logging

Run tests with:

```
RUST_LOG=debug cargo bench
```

with instrument attribute
                        time:   [97.059 µs 97.660 µs 98.264 µs]
                        change: [-1.1771% +0.6401% +2.3332%] (p = 0.48 > 0.05)
                        No change in performance detected.
Found 7 outliers among 100 measurements (7.00%)
  2 (2.00%) low severe
  1 (1.00%) low mild
  3 (3.00%) high mild
  1 (1.00%) high severe


with debug! macro       time:   [91.455 µs 91.923 µs 92.404 µs]
                        change: [-3.3669% -1.0630% +0.8389%] (p = 0.36 > 0.05)
                        No change in performance detected.
Found 8 outliers among 100 measurements (8.00%)
  1 (1.00%) low severe
  1 (1.00%) low mild
  5 (5.00%) high mild
  1 (1.00%) high severe