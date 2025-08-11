-- LIST PARTITIONING EXAMPLE
-- Partitioning products data by category (discrete values)

-- Create parent table for list partitioning
CREATE TABLE products_list_partition (
    product_id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    supplier_id INTEGER NOT NULL,
    created_date DATE NOT NULL
) PARTITION BY LIST (category);

-- Create partitions for different categories
CREATE TABLE products_electronics PARTITION OF products_list_partition
    FOR VALUES IN ('electronics', 'computers', 'phones');

CREATE TABLE products_clothing PARTITION OF products_list_partition
    FOR VALUES IN ('clothing', 'shoes', 'accessories');

CREATE TABLE products_books PARTITION OF products_list_partition
    FOR VALUES IN ('books', 'magazines', 'ebooks');

CREATE TABLE products_home PARTITION OF products_list_partition
    FOR VALUES IN ('furniture', 'appliances', 'decor');

CREATE TABLE products_sports PARTITION OF products_list_partition
    FOR VALUES IN ('sports', 'fitness', 'outdoor');

-- Default partition for any category not explicitly listed
CREATE TABLE products_other PARTITION OF products_list_partition DEFAULT;

-- Create indexes on partitions
CREATE INDEX idx_products_electronics_price ON products_electronics (price);
CREATE INDEX idx_products_clothing_price ON products_clothing (price);
CREATE INDEX idx_products_books_price ON products_books (price);
CREATE INDEX idx_products_home_price ON products_home (price);
CREATE INDEX idx_products_sports_price ON products_sports (price);
CREATE INDEX idx_products_other_price ON products_other (price);

CREATE INDEX idx_products_electronics_supplier ON products_electronics (supplier_id);
CREATE INDEX idx_products_clothing_supplier ON products_clothing (supplier_id);
CREATE INDEX idx_products_books_supplier ON products_books (supplier_id);
CREATE INDEX idx_products_home_supplier ON products_home (supplier_id);
CREATE INDEX idx_products_sports_supplier ON products_sports (supplier_id);
CREATE INDEX idx_products_other_supplier ON products_other (supplier_id);

-- Insert sample data
INSERT INTO products_list_partition (product_name, category, price, stock_quantity, supplier_id, created_date) VALUES
    ('Laptop', 'electronics', 999.99, 50, 1, '2023-01-15'),
    ('Smartphone', 'phones', 699.99, 100, 2, '2023-02-20'),
    ('Gaming Headset', 'electronics', 149.99, 75, 1, '2023-03-10'),
    ('T-Shirt', 'clothing', 29.99, 200, 3, '2023-04-05'),
    ('Running Shoes', 'shoes', 89.99, 80, 4, '2023-05-18'),
    ('Programming Book', 'books', 49.99, 30, 5, '2023-06-12'),
    ('Office Chair', 'furniture', 299.99, 25, 6, '2023-07-25'),
    ('Coffee Maker', 'appliances', 129.99, 40, 7, '2023-08-08'),
    ('Basketball', 'sports', 24.99, 60, 8, '2023-09-20'),
    ('Yoga Mat', 'fitness', 39.99, 90, 8, '2023-10-30'),
    ('Wall Art', 'decor', 79.99, 20, 9, '2023-11-15'),
    ('Mystery Novel', 'books', 14.99, 150, 5, '2023-12-01'),
    ('Winter Jacket', 'clothing', 159.99, 35, 3, '2023-12-15'),
    ('Camping Tent', 'outdoor', 249.99, 15, 10, '2023-12-20'),
    ('Kitchen Utensils', 'kitchenware', 49.99, 45, 11, '2023-12-25'); -- This will go to default partition

-- Demonstration queries
SELECT 'List Partitioning Demonstrations:' as demo_type;

-- Query 1: Select products from electronics category (partition pruning will occur)
EXPLAIN (ANALYZE, BUFFERS) 
SELECT * FROM products_list_partition 
WHERE category IN ('electronics', 'phones');

-- Query 2: Count products by category and show which partition they're in
SELECT 
    category,
    COUNT(*) as product_count,
    AVG(price) as avg_price,
    tableoid::regclass as partition_table
FROM products_list_partition 
GROUP BY category, tableoid::regclass
ORDER BY category;

-- Query 3: Find expensive products across all categories
SELECT product_name, category, price, tableoid::regclass as partition_table
FROM products_list_partition 
WHERE price > 100.00
ORDER BY price DESC;

-- Query 4: Show partition-specific statistics
SELECT 
    schemaname,
    tablename,
    n_tup_ins as rows_inserted,
    n_tup_upd as rows_updated,
    n_tup_del as rows_deleted,
    n_tup_hot_upd as hot_updates
FROM pg_stat_user_tables 
WHERE tablename LIKE 'products_%'
ORDER BY tablename;

-- Query 5: Demonstrate constraint exclusion with specific categories
EXPLAIN (ANALYZE, BUFFERS) 
SELECT product_name, price 
FROM products_list_partition 
WHERE category = 'books' AND price < 30.00;