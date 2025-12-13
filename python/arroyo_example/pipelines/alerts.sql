CREATE TABLE btc_window_1m_source (
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
    topic = 'btc-window-1m',
    type = 'source',
    format = 'json'
);

CREATE TABLE btc_alerts (
    symbol TEXT,
    alert_type TEXT,
    severity TEXT,
    message TEXT,
    current_price FLOAT,
    reference_price FLOAT,
    change_pct FLOAT,
    triggered_at TEXT,
    window_size_sec BIGINT
) WITH (
    connector = 'kafka',
    bootstrap_servers = 'kafka:9092',
    topic = 'btc-alerts',
    type = 'sink',
    format = 'json'
);

INSERT INTO btc_alerts
SELECT
    symbol,
    CASE 
        WHEN price_change_pct > 0 THEN 'PRICE_SPIKE'
        ELSE 'PRICE_DROP'
    END,
    'HIGH',
    'Price moved significantly',
    close_price,
    open_price,
    price_change_pct,
    window_end,
    window_size_sec
FROM btc_window_1m_source
WHERE abs(price_change_pct) >= 0.0001;
