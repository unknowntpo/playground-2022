# Arroyo stream processing example

## Architecture

Arroyo (Single Node) + 3 node AutoMQ + MinIO + Redpanda console + Redis + Streamlit

```text
+-------------------+           +-----------------------+           +-------------------+
|  price-producer   |           |     Arroyo Server     |           |   arroyo-init     |
|  (yliveticker)    |           |   (SQL Processing)    | <-------- | (Submits SQL)     |
+---------+---------+           +-----------+-----------+           +-------------------+
          |                                 ^
          | produces                        | reads/writes
          v                                 v
+---------+---------------------------------+-----------------------------------+
|                          AutoMQ (Kafka)                                       |
|                                                                               |
|  [btc-price-ticks] --------> [Arroyo] --------> [btc-window-1m]               |
|                                  |                                            |
|                                  +------------> [btc-window-5m]               |
|                                  |                                            |
|                                  +------------> [btc-alerts]                  |
+----------------------------------+--------------------------------------------+
                                   |
                                   | consumes processed topics
                                   v
+-------------------+      +-------+-------+      +-------------------+
|     Dashboard     | <--- |     Redis     | <--- |   redis-writer    |
|    (Streamlit)    | reads|               | writes| (Python Consumer)|
+-------------------+      +---------------+      +-------------------+
```

## Setup Links

- https://doc.arroyo.dev/getting-started