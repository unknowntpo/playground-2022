use pgx::prelude::*;
// use serde::{Deserialize, Serialize};

pgx::pg_module_magic!();

#[pg_extern]
fn hello_hello_world() -> &'static str {
    "Hello, hello_world"
}

#[cfg(any(test, feature = "pg_test"))]
#[pg_schema]
mod tests {
    use pgx::prelude::*;

    #[pg_test]
    fn test_hello_hello_world() {
        assert_eq!("Hello, hello_world", crate::hello_hello_world());
    }
}

/// This module is required by `cargo pgx test` invocations.
/// It must be visible at the root of your extension crate.
#[cfg(test)]
pub mod pg_test {
    pub fn setup(_options: Vec<&str>) {
        // perform one-off initialization when the pg_test framework starts
    }

    pub fn postgresql_conf_options() -> Vec<&'static str> {
        // return any postgresql.conf settings that are required for your tests
        vec![]
    }
}

use pgx::hooks::*;
use pgx::pg_sys::*;
use pgx::*;

// #[pg_guard]
// fn my_planner_hook(query: &str, plan: &PlannedStmt) -> bool {
//     // Print the query text and the plan type
//     println!("Query: {}", query);
//     println!("Plan type: {:?}", plan);

//     // Return true to allow the planner to continue with its normal processing
//     true
// }

#[pg_guard]
pub extern "C" fn _PG_init() {
    // Register the custom planner hook
    // let hook = PgHooks{}
    // let hook = hooks.    {
    //     hook_next: None,
    //     planner: my_planner_hook,
    // };
    // let hook_ptr = Box::into_raw(Box::new(hook));
    // unsafe {
    // let hook_name = CString::new("my_planner_hook").unwrap();
    // pg_register_planner_hook(hook_ptr, hook_name.as_ptr());
    // }
    static mut hook: MyHook = MyHook {};
    unsafe {
        let pHook: &'static mut (dyn PgHooks) = &mut hook;
        hooks::register_hook(pHook);
    }
}

fn _PG_fini() {}

struct MyHook {}

use core::ffi::CStr;
use std::os::raw::c_char;

impl PgHooks for MyHook {
    fn planner(
        &mut self,
        parse: PgBox<Query>,
        query_string: *const c_char,
        cursor_options: i32,
        bound_params: PgBox<ParamListInfoData>,
        prev_hook: fn(
            parse: PgBox<Query>,
            query_string: *const c_char,
            cursor_options: i32,
            bound_params: PgBox<ParamListInfoData>,
        ) -> HookResult<*mut PlannedStmt>,
    ) -> HookResult<*mut PlannedStmt> {
        println!(
            "Planner hook is called for query: {:?}",
            c_str_to_rust_string(query_string)
        );
        prev_hook(parse, query_string, cursor_options, bound_params)
    }
}

fn c_str_to_rust_string(c_str: *const c_char) -> String {
    let c_slice = unsafe { CStr::from_ptr(c_str) };
    c_slice.to_str().unwrap().to_owned()
}
