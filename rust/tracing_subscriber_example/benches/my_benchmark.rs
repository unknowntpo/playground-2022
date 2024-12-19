use criterion::{criterion_group, criterion_main, Criterion};

use std::hint::black_box;

fn fibonacci(a: i32) -> i32 {
    if a <= 2 {
        return a;
    };
    fibonacci(a - 1) + fibonacci(a - 2)
}

fn bench_attr(c: &mut Criterion) {
    c.bench_function("fib 20", |b| b.iter(|| fibonacci(black_box(20))));
}

criterion_group!(benches, bench_attr);
criterion_main!(benches);
