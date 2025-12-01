-- Create pg_parquet extension
CREATE EXTENSION IF NOT EXISTS pg_parquet;

-- Create a regular table with sample data
CREATE TABLE sales_data (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(100),
    category VARCHAR(50),
    quantity INTEGER,
    price DECIMAL(10, 2),
    sale_date DATE,
    region VARCHAR(50)
);

-- Insert test data
INSERT INTO sales_data (product_name, category, quantity, price, sale_date, region) VALUES
    ('Laptop Pro 15', 'Electronics', 5, 1299.99, '2024-01-15', 'North America'),
    ('Wireless Mouse', 'Electronics', 25, 29.99, '2024-01-16', 'Europe'),
    ('USB-C Cable', 'Accessories', 100, 12.99, '2024-01-17', 'Asia'),
    ('Monitor 27"', 'Electronics', 10, 399.99, '2024-01-18', 'North America'),
    ('Keyboard Mechanical', 'Electronics', 15, 89.99, '2024-01-19', 'Europe'),
    ('Desk Lamp', 'Furniture', 30, 45.50, '2024-01-20', 'Asia'),
    ('Office Chair', 'Furniture', 8, 249.99, '2024-01-21', 'North America'),
    ('Webcam HD', 'Electronics', 20, 79.99, '2024-01-22', 'Europe'),
    ('Headphones', 'Electronics', 35, 129.99, '2024-01-23', 'Asia'),
    ('Phone Case', 'Accessories', 150, 19.99, '2024-01-24', 'North America');

-- Export to Parquet file (requires pg_parquet)
-- We copy to a location inside the container
COPY sales_data TO '/tmp/sales_data.parquet' (FORMAT 'parquet');

-- Create a table to import back into
CREATE TABLE sales_parquet_import (LIKE sales_data INCLUDING ALL);

-- Import from Parquet file
COPY sales_parquet_import FROM '/tmp/sales_data.parquet' (FORMAT 'parquet');

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
