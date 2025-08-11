-- PERFORMANCE DEMONSTRATION AND COMPARISON
-- Comparing partitioned vs non-partitioned tables

-- Create non-partitioned comparison tables
CREATE TABLE sales_non_partitioned (
    id BIGSERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    sale_date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    region VARCHAR(50) NOT NULL
);

CREATE TABLE users_non_partitioned (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL,
    country VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Create indexes on non-partitioned tables (similar to partitioned ones)
CREATE INDEX idx_sales_non_part_date ON sales_non_partitioned (sale_date);
CREATE INDEX idx_sales_non_part_customer ON sales_non_partitioned (customer_id);
CREATE INDEX idx_users_non_part_email ON users_non_partitioned (email);
CREATE INDEX idx_users_non_part_username ON users_non_partitioned (username);

-- Function to generate large dataset for performance testing
CREATE OR REPLACE FUNCTION populate_sales_data(num_records INTEGER)
RETURNS VOID AS $$
DECLARE
    i INTEGER;
    start_date DATE := '2023-01-01';
    end_date DATE := '2024-12-31';
BEGIN
    -- Insert into non-partitioned table
    FOR i IN 1..num_records LOOP
        INSERT INTO sales_non_partitioned (customer_id, product_id, sale_date, amount, region)
        VALUES (
            (random() * 1000)::INTEGER + 1,
            (random() * 500)::INTEGER + 1,
            start_date + (random() * (end_date - start_date))::INTEGER,
            (random() * 5000)::DECIMAL(10,2) + 10,
            (ARRAY['North', 'South', 'East', 'West'])[floor(random() * 4 + 1)]
        );
    END LOOP;
    
    -- Insert same data pattern into partitioned table
    INSERT INTO sales_range_partition (customer_id, product_id, sale_date, amount, region)
    SELECT customer_id, product_id, sale_date, amount, region 
    FROM sales_non_partitioned;
END;
$$ LANGUAGE plpgsql;

-- Function to generate user data
CREATE OR REPLACE FUNCTION populate_users_data(num_records INTEGER)
RETURNS VOID AS $$
DECLARE
    i INTEGER;
    countries TEXT[] := ARRAY['USA', 'Canada', 'UK', 'Germany', 'France', 'Spain', 'Italy', 'Australia', 'Japan', 'Brazil'];
    statuses TEXT[] := ARRAY['active', 'inactive', 'suspended'];
BEGIN
    -- Insert into non-partitioned table
    FOR i IN 1..num_records LOOP
        INSERT INTO users_non_partitioned (username, email, registration_date, country, status)
        VALUES (
            'user_' || i,
            'user' || i || '@example.com',
            '2023-01-01'::DATE + (random() * 365)::INTEGER,
            countries[floor(random() * array_length(countries, 1) + 1)],
            statuses[floor(random() * array_length(statuses, 1) + 1)]
        );
    END LOOP;
    
    -- Insert same data pattern into partitioned table
    INSERT INTO users_hash_partition (username, email, registration_date, country, status)
    SELECT username, email, registration_date, country, status 
    FROM users_non_partitioned;
END;
$$ LANGUAGE plpgsql;

-- Performance comparison queries
SELECT 'Performance Comparison Tests:' as demo_type;

-- Test 1: Range partition vs non-partitioned for date range queries
SELECT 'Test 1: Date range query performance' as test_name;

-- Partitioned table query
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT COUNT(*), SUM(amount) 
FROM sales_range_partition 
WHERE sale_date >= '2023-07-01' AND sale_date < '2023-10-01';

-- Non-partitioned table query
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT COUNT(*), SUM(amount) 
FROM sales_non_partitioned 
WHERE sale_date >= '2023-07-01' AND sale_date < '2023-10-01';

-- Test 2: Hash partition vs non-partitioned for specific ID lookups
SELECT 'Test 2: ID lookup performance' as test_name;

-- Partitioned table query
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT * FROM users_hash_partition WHERE user_id = 500;

-- Non-partitioned table query
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT * FROM users_non_partitioned WHERE user_id = 500;

-- Maintenance operations demonstration
SELECT 'Maintenance Operations:' as demo_type;

-- Show partition information
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables 
WHERE tablename LIKE 'sales_%' OR tablename LIKE 'users_%' OR tablename LIKE 'products_%'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Show partition constraints
SELECT 
    conname as constraint_name,
    conrelid::regclass as table_name,
    pg_get_constraintdef(oid) as constraint_definition
FROM pg_constraint 
WHERE contype = 'c' 
AND conrelid::regclass::text LIKE '%partition%'
ORDER BY conrelid::regclass;

-- Demonstrate partition pruning in action
SELECT 'Partition Pruning Examples:' as demo_type;

-- This query should only scan Q3 2023 partition
EXPLAIN (BUFFERS, FORMAT TEXT)
SELECT * FROM sales_range_partition 
WHERE sale_date = '2023-08-15';

-- This query should scan multiple partitions
EXPLAIN (BUFFERS, FORMAT TEXT)
SELECT * FROM sales_range_partition 
WHERE sale_date >= '2023-06-01' AND sale_date <= '2023-11-30';

-- Best practices and tips
SELECT 'Best Practices Summary:' as summary;
SELECT '1. Range partitioning is ideal for time-series data' as tip
UNION ALL
SELECT '2. Hash partitioning provides even data distribution' as tip
UNION ALL
SELECT '3. List partitioning works well for categorical data' as tip
UNION ALL
SELECT '4. Use partition pruning to improve query performance' as tip
UNION ALL
SELECT '5. Create appropriate indexes on each partition' as tip
UNION ALL
SELECT '6. Monitor partition sizes and add new partitions as needed' as tip
UNION ALL
SELECT '7. Use constraint exclusion for better query planning' as tip;