CREATE TABLE btc_price_ticks (
    symbol TEXT,
    price FLOAT,
    timestamp TIMESTAMP NOT NULL,
    change FLOAT,
    change_percent FLOAT,
    day_high FLOAT,
    day_low FLOAT,
    volume FLOAT,
    WATERMARK FOR timestamp AS timestamp - INTERVAL '5 seconds'
) WITH (
    connector = 'kafka',
    bootstrap_servers = 'kafka:9092',
    topic = 'btc-price-ticks',
    type = 'source',
    format = 'json'
);

CREATE TABLE btc_window_5m (
    symbol TEXT,
    window_start TEXT,
    window_end TEXT,
    window_size_sec BIGINT,
    open_price FLOAT,
    close_price FLOAT,
    high_price FLOAT,
    low_price FLOAT,
    avg_price FLOAT,
    price_change FLOAT,
    price_change_pct FLOAT,
    tick_count BIGINT
) WITH (
    connector = 'kafka',
    bootstrap_servers = 'kafka:9092',
    topic = 'btc-window-5m',
    type = 'sink',
    format = 'json'
);

INSERT INTO btc_window_5m
SELECT
    symbol,
    '2024-01-01T00:00:00',
    '2024-01-01T00:00:30',
    30,
    first_value(price ORDER BY timestamp),
    last_value(price ORDER BY timestamp),
    max(price),
    min(price),
    avg(price),
    last_value(price ORDER BY timestamp) - first_value(price ORDER BY timestamp),
    (last_value(price ORDER BY timestamp) - first_value(price ORDER BY timestamp)) / first_value(price ORDER BY timestamp) * 100,
    count(*)
FROM btc_price_ticks
GROUP BY
    tumble(interval '30 seconds'),
    symbol;