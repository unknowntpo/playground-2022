use datafusion::prelude::*;

// https://datafusion.apache.org/user-guide/example-usage.html#run-a-sql-query-against-data-stored-in-a-csv

#[tokio::main]
async fn main() -> datafusion::error::Result<()> {
    let ctx = SessionContext::new();
    ctx.register_csv("example", "tests/data/example.csv", CsvReadOptions::new())
        .await?;

    let df = ctx
        .sql("SELECT a, MIN(b) FROM example WHERE a <= b GROUP by a LIMIT 100")
        .await?;
    df.show().await?;
    Ok(())
}
