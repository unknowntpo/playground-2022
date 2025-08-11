-- HASH PARTITIONING EXAMPLE
-- Partitioning users data by hash of user_id for even distribution

-- Create parent table for hash partitioning
CREATE TABLE users_hash_partition (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL,
    country VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
) PARTITION BY HASH (user_id);

-- Create hash partitions (4 partitions for demonstration)
CREATE TABLE users_hash_0 PARTITION OF users_hash_partition
    FOR VALUES WITH (modulus 4, remainder 0);

CREATE TABLE users_hash_1 PARTITION OF users_hash_partition
    FOR VALUES WITH (modulus 4, remainder 1);

CREATE TABLE users_hash_2 PARTITION OF users_hash_partition
    FOR VALUES WITH (modulus 4, remainder 2);

CREATE TABLE users_hash_3 PARTITION OF users_hash_partition
    FOR VALUES WITH (modulus 4, remainder 3);

-- Create indexes on each partition
CREATE INDEX idx_users_hash_0_email ON users_hash_0 (email);
CREATE INDEX idx_users_hash_1_email ON users_hash_1 (email);
CREATE INDEX idx_users_hash_2_email ON users_hash_2 (email);
CREATE INDEX idx_users_hash_3_email ON users_hash_3 (email);

CREATE INDEX idx_users_hash_0_username ON users_hash_0 (username);
CREATE INDEX idx_users_hash_1_username ON users_hash_1 (username);
CREATE INDEX idx_users_hash_2_username ON users_hash_2 (username);
CREATE INDEX idx_users_hash_3_username ON users_hash_3 (username);

-- Insert sample data
INSERT INTO users_hash_partition (username, email, registration_date, country, status) VALUES
    ('john_doe', 'john@example.com', '2023-01-15', 'USA', 'active'),
    ('jane_smith', 'jane@example.com', '2023-02-20', 'Canada', 'active'),
    ('bob_wilson', 'bob@example.com', '2023-03-10', 'UK', 'inactive'),
    ('alice_brown', 'alice@example.com', '2023-04-05', 'Australia', 'active'),
    ('charlie_davis', 'charlie@example.com', '2023-05-18', 'Germany', 'active'),
    ('eva_martinez', 'eva@example.com', '2023-06-12', 'Spain', 'inactive'),
    ('david_taylor', 'david@example.com', '2023-07-25', 'France', 'active'),
    ('sarah_white', 'sarah@example.com', '2023-08-08', 'Italy', 'active'),
    ('mike_johnson', 'mike@example.com', '2023-09-20', 'Japan', 'inactive'),
    ('lisa_garcia', 'lisa@example.com', '2023-10-30', 'Mexico', 'active'),
    ('tom_anderson', 'tom@example.com', '2023-11-15', 'Brazil', 'active'),
    ('amy_thompson', 'amy@example.com', '2023-12-01', 'India', 'active');

-- Demonstration queries
SELECT 'Hash Partitioning Demonstrations:' as demo_type;

-- Query 1: Find user by ID (hash partitioning will route to specific partition)
EXPLAIN (ANALYZE, BUFFERS) 
SELECT * FROM users_hash_partition WHERE user_id = 5;

-- Query 2: Check data distribution across partitions
SELECT 
    schemaname,
    tablename,
    n_tup_ins as rows_inserted,
    n_tup_upd as rows_updated,
    n_tup_del as rows_deleted
FROM pg_stat_user_tables 
WHERE tablename LIKE 'users_hash_%'
ORDER BY tablename;

-- Query 3: Count users by status across all partitions
SELECT status, COUNT(*) as user_count 
FROM users_hash_partition 
GROUP BY status;

-- Query 4: Show which partition each user ended up in
SELECT 
    user_id,
    username,
    user_id % 4 as expected_partition,
    tableoid::regclass as actual_partition_table
FROM users_hash_partition 
ORDER BY user_id;