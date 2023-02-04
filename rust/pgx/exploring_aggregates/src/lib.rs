use pgx::*;
use serde::{Deserialize, Serialize};

pg_module_magic!();

#[derive(Copy, Clone, Default, Debug, PostgresType, Serialize, Deserialize)]
pub struct DemoSum {
    count: i32,
}

#[pg_aggregate]
impl Aggregate for DemoSum {
    const INITIAL_CONDITION: Option<&'static str> = Some(r#"{ "count": 0 }"#);
    type Args = i32;
    fn state(
        mut current: Self::State,
        arg: Self::Args,
        _fcinfo: pg_sys::FunctionCallInfo,
    ) -> Self::State {
        current.count += arg;
        current
    }
}
