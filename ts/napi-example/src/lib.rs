#![deny(clippy::all)]

use futures::prelude::*;
use napi::bindgen_prelude::*;
use napi::tokio::fs;

#[macro_use]
extern crate napi_derive;

#[napi]
pub fn sum(a: i32, b: i32) -> i32 {
  a + b
}

#[napi]
async fn read_file_async(path: String) -> Result<Buffer> {
  fs::read(path)
    .map(|r| match r {
      Ok(content) => Ok(content.into()),
      Err(e) => Err(Error::new(
        Status::GenericFailure,
        format!("failed to read file, {}", e),
      )),
    })
    .await
}

#[napi]
fn busy_loop_rs(n: i32) -> i32 {
  let mut i: i32 = 0;
  while i < n {
    i += 1;
  }
  return i;
}

#[napi]
fn call_with_one<F>(n: i32, func: F) -> Result<i32>
where
  F: Fn(i32) -> Result<i32>,
{
  return func(n);
}
