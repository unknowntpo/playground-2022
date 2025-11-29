-- Step 1: Create the source table reading from the mounted /data directory
-- This reads the events.jsonl file which contains our simulated data.
CREATE TABLE events (
    user_id TEXT,
    type TEXT,
    time TIMESTAMP
) WITH (
    connector = 'filesystem',
    path = '/data/events.jsonl',
    format = 'json',
    type = 'source',
    event_time_field = 'time'
);

-- Step 2: Define the tumbling window aggregation
-- This counts events per user every 5 seconds, similar to the Bytewax example.
SELECT
    user_id,
    TUMBLE(interval '5 seconds') as window,
    count(*) as event_count
FROM events
GROUP BY user_id, window;
