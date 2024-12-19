use criterion::{criterion_group, criterion_main, Criterion};
use log::debug;
use tracing::instrument;

use std::hint::black_box;

fn fibonacci(a: i32) -> i32 {
    if a <= 2 {
        return a;
    };
    fibonacci(a - 1) + fibonacci(a - 2)
}

#[derive(Debug)]
struct User {
    id: i32,
    name: String,
}

#[instrument(level = "debug", ret, err)]
fn outer(u: User) -> Result<User, String> {
    middle(u, 3, 2)
}

#[instrument(level = "debug", ret, err)]
fn middle(u: User, a: i32, b: i32) -> Result<User, String> {
    inner(u, a + 1, b + 1)
}

#[instrument(level = "debug", ret, err)]
fn inner(u: User, d: i32, e: i32) -> Result<User, String> {
    Ok(u)
}

/// with debug! logging
fn outer_debug(u: User) -> Result<User, String> {
    debug!("Entering outer_debug with u: {:?}", u);
    let result = middle(u, 3, 2);
    debug!("Exiting outer_debug with result: {:?}", result);
    result
}

fn middle_debug(u: User, a: i32, b: i32) -> Result<User, String> {
    debug!("Entering middle with u: {:?}, a: {}, b: {}", u, a, b);
    let result = inner(u, a + 1, b + 1);
    debug!("Exiting middle with result: {:?}", result);
    result
}

fn inner_debug(u: User, d: i32, e: i32) -> Result<User, String> {
    debug!("Entering inner with u: {:?}, d: {}, e: {}", u, d, e);
    let result = Ok(u);
    debug!("Exiting inner with result: {:?}", result);
    result
}

fn bench_attr(c: &mut Criterion) {
    tracing_subscriber::fmt::init();
    // c.bench_function("fib 20", |b| b.iter(|| fibonacci(black_box(20))));
    c.bench_function("with instrument attribute", |b| {
        b.iter(|| {
            outer(User {
                id: 3,
                name: "Hello".to_string(),
            })
        })
    });
    c.bench_function("with debug! macro", |b| {
        b.iter(|| {
            outer_debug(User {
                id: 3,
                name: "Hello".to_string(),
            })
        })
    });
}

criterion_group!(benches, bench_attr);
criterion_main!(benches);
