-- PostgreSQL Database Partitioning Examples
-- This file demonstrates various partitioning strategies in PostgreSQL

-- Drop existing objects if they exist
DROP TABLE IF EXISTS sales_range_partition CASCADE;
DROP TABLE IF EXISTS users_hash_partition CASCADE;
DROP TABLE IF EXISTS products_list_partition CASCADE;
DROP TABLE IF EXISTS sales_non_partitioned CASCADE;
DROP TABLE IF EXISTS users_non_partitioned CASCADE;

-- Drop functions if they exist
DROP FUNCTION IF EXISTS populate_sales_data(INTEGER);
DROP FUNCTION IF EXISTS populate_users_data(INTEGER);

-- Enable timing for performance demonstrations
\timing on

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";