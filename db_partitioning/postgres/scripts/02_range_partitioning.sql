-- RANGE PARTITIONING EXAMPLE
-- Partitioning sales data by date ranges

-- Create parent table for range partitioning
CREATE TABLE sales_range_partition (
    id BIGSERIAL,
    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    sale_date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    region VARCHAR(50) NOT NULL
) PARTITION BY RANGE (sale_date);

-- Create partitions for different date ranges
CREATE TABLE sales_2023_q1 PARTITION OF sales_range_partition
    FOR VALUES FROM ('2023-01-01') TO ('2023-04-01');

CREATE TABLE sales_2023_q2 PARTITION OF sales_range_partition
    FOR VALUES FROM ('2023-04-01') TO ('2023-07-01');

CREATE TABLE sales_2023_q3 PARTITION OF sales_range_partition
    FOR VALUES FROM ('2023-07-01') TO ('2023-10-01');

CREATE TABLE sales_2023_q4 PARTITION OF sales_range_partition
    FOR VALUES FROM ('2023-10-01') TO ('2024-01-01');

CREATE TABLE sales_2024_q1 PARTITION OF sales_range_partition
    FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');

-- Create indexes on partitions for better performance
CREATE INDEX idx_sales_2023_q1_customer ON sales_2023_q1 (customer_id);
CREATE INDEX idx_sales_2023_q2_customer ON sales_2023_q2 (customer_id);
CREATE INDEX idx_sales_2023_q3_customer ON sales_2023_q3 (customer_id);
CREATE INDEX idx_sales_2023_q4_customer ON sales_2023_q4 (customer_id);
CREATE INDEX idx_sales_2024_q1_customer ON sales_2024_q1 (customer_id);

-- Insert sample data
INSERT INTO sales_range_partition (customer_id, product_id, sale_date, amount, region) VALUES
    (1, 101, '2023-01-15', 1200.00, 'North'),
    (2, 102, '2023-02-20', 850.50, 'South'),
    (3, 103, '2023-03-10', 2100.75, 'East'),
    (4, 104, '2023-05-05', 1500.00, 'West'),
    (5, 105, '2023-06-18', 900.25, 'North'),
    (6, 106, '2023-08-12', 1750.00, 'South'),
    (7, 107, '2023-09-25', 1100.50, 'East'),
    (8, 108, '2023-11-08', 2200.00, 'West'),
    (9, 109, '2023-12-20', 950.75, 'North'),
    (10, 110, '2024-01-30', 1800.00, 'South');

-- Demonstration queries
SELECT 'Range Partitioning Demonstrations:' as demo_type;

-- Query 1: Select data from a specific quarter (partition pruning will occur)
EXPLAIN (ANALYZE, BUFFERS) 
SELECT * FROM sales_range_partition 
WHERE sale_date >= '2023-04-01' AND sale_date < '2023-07-01';

-- Query 2: Count sales by quarter
SELECT 
    CASE 
        WHEN sale_date >= '2023-01-01' AND sale_date < '2023-04-01' THEN 'Q1 2023'
        WHEN sale_date >= '2023-04-01' AND sale_date < '2023-07-01' THEN 'Q2 2023'
        WHEN sale_date >= '2023-07-01' AND sale_date < '2023-10-01' THEN 'Q3 2023'
        WHEN sale_date >= '2023-10-01' AND sale_date < '2024-01-01' THEN 'Q4 2023'
        WHEN sale_date >= '2024-01-01' AND sale_date < '2024-04-01' THEN 'Q1 2024'
    END as quarter,
    COUNT(*) as sales_count,
    SUM(amount) as total_amount
FROM sales_range_partition 
GROUP BY quarter
ORDER BY quarter;