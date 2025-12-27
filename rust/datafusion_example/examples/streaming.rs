use std::sync::Arc;

use datafusion::arrow::array::{Int64Array, TimestampNanosecondArray};
use datafusion::arrow::datatypes::{DataType, Field, Schema, SchemaRef, TimeUnit};
use datafusion::arrow::record_batch::RecordBatch;

use datafusion::catalog::streaming::StreamingTable;
use datafusion::error::Result;
use datafusion::execution::TaskContext;
use datafusion::physical_plan::SendableRecordBatchStream;
use datafusion::physical_plan::stream::RecordBatchStreamAdapter;
use datafusion::physical_plan::streaming::PartitionStream;
use datafusion::prelude::*;

use futures::StreamExt;
use tokio::time::{Duration, sleep};

#[tokio::main]
async fn main() -> Result<()> {
    // Define the schema
    let schema = Arc::new(Schema::new(vec![
        Field::new("ts", DataType::Timestamp(TimeUnit::Nanosecond, None), false),
        Field::new("value", DataType::Int64, false),
    ]));

    // Create our custom partition stream (one partition is enough for simulation)
    let partition = SimulatedPartitionStream::new(schema.clone());

    // Create the streaming table (single partition)
    let streaming_table = StreamingTable::try_new(schema, vec![Arc::new(partition)])?;

    // Mark as infinite/unbounded so windowing works continuously
    let streaming_table = Arc::new(streaming_table.with_infinite_table(true));

    // Register in context
    let ctx = SessionContext::new();
    ctx.register_table("streaming_events", streaming_table)?;

    // Simple streaming query - just select and display data as it arrives
    let df = ctx
        .sql(
            r#"
        SELECT
            ts,
            value
        FROM streaming_events
        LIMIT 50
        "#,
        )
        .await?;

    let mut stream = df.execute_stream().await?;

    println!("Streaming query started – showing first 50 events:\n");

    while let Some(batch) = stream.next().await {
        let batch = batch?;
        if batch.num_rows() == 0 {
            continue;
        }

        let pretty = datafusion::arrow::util::pretty::pretty_format_batches(&[batch])?;
        println!("{pretty}\n");
    }

    Ok(())
}

// Custom partition that produces an infinite stream of RecordBatches
#[derive(Debug)]
struct SimulatedPartitionStream {
    schema: SchemaRef,
}

impl SimulatedPartitionStream {
    fn new(schema: SchemaRef) -> Self {
        Self { schema }
    }
}

impl PartitionStream for SimulatedPartitionStream {
    fn schema(&self) -> &SchemaRef {
        &self.schema
    }

    fn execute(&self, _ctx: Arc<TaskContext>) -> SendableRecordBatchStream {
        let schema = self.schema.clone();

        // Infinite async stream
        let stream = async_stream::stream! {
            let mut base_ts = chrono::Utc::now().timestamp_nanos_opt().unwrap_or(0);

            loop {
                // Generate 1000 events per batch
                let timestamps: Vec<i64> = (0..1000)
                    .map(|i| base_ts + i * 1_000_000) // 1ms spacing
                    .collect();
                let values: Vec<i64> = (0..1000).map(|i| i % 100).collect();

                let batch = RecordBatch::try_new(
                    schema.clone(),
                    vec![
                        Arc::new(TimestampNanosecondArray::from(timestamps)),
                        Arc::new(Int64Array::from(values)),
                    ],
                ).unwrap();

                yield Ok(batch);

                // Advance time by 1 second for next batch
                base_ts += 1_000_000_000;

                // Simulate real-world ingestion rate
                sleep(Duration::from_millis(100)).await;
            }
        };

        Box::pin(RecordBatchStreamAdapter::new(self.schema.clone(), stream))
    }
}
