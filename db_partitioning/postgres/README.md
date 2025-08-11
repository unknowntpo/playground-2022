# PostgreSQL Database Partitioning Examples

This directory contains comprehensive examples of PostgreSQL database partitioning strategies with Docker and Just for easy execution.

## 🚀 Quick Start (One-Click Demo)

```bash
# Install just (if not already installed)
# macOS: brew install just
# Ubuntu: sudo apt install just

# Run all partitioning examples
just demo
```

## 📁 Files Overview

- **docker-compose.yml** - PostgreSQL container setup
- **justfile** - Task runner with organized commands
- **scripts/** - SQL partitioning examples
  - `01_setup.sql` - Initial setup and cleanup
  - `02_range_partitioning.sql` - Range partitioning (date-based)
  - `03_hash_partitioning.sql` - Hash partitioning (user distribution)
  - `04_list_partitioning.sql` - List partitioning (category-based)
  - `05_performance_demo.sql` - Performance comparisons

## 🎯 Available Commands

```bash
# Run complete demo
just demo                    # Run all examples + connect info

# Individual examples
just range-example          # Range partitioning only
just hash-example           # Hash partitioning only  
just list-example           # List partitioning only

# Database operations
just up                     # Start PostgreSQL
just connect               # Connect to database
just show-partitions       # View partition info
just performance-test      # Run with 10k records

# Maintenance
just down                  # Stop containers
just clean                 # Clean slate (remove volumes)
just fresh-demo           # Clean + full demo
```

## 📊 Partitioning Types Demonstrated

### Range Partitioning
- **Use case**: Time-series data, ordered data ranges
- **Example**: Sales data partitioned by quarters
- **Benefits**: Efficient date range queries, easy maintenance

### Hash Partitioning  
- **Use case**: Even data distribution across partitions
- **Example**: Users partitioned by user_id hash
- **Benefits**: Balanced partition sizes, good for parallel processing

### List Partitioning
- **Use case**: Categorical data with discrete values
- **Example**: Products partitioned by category
- **Benefits**: Logical data separation, efficient category-based queries

## 🔧 Manual Setup (Alternative)

```bash
# Start database
docker-compose up -d

# Connect manually
docker-compose exec postgres psql -U demo_user -d partitioning_demo

# Run scripts manually
\i /docker-entrypoint-initdb.d/01_setup.sql
\i /docker-entrypoint-initdb.d/02_range_partitioning.sql
# ... etc
```

## ⚡ Key Benefits of Partitioning

- **Query Performance**: Partition pruning eliminates unnecessary partition scans
- **Maintenance**: Operations can target specific partitions
- **Parallel Processing**: Queries can run across partitions simultaneously
- **Storage Management**: Older partitions can be archived or dropped
- **Scalability**: Large tables become more manageable

## 📝 Important Notes

- Partitioning works best with large tables (millions of rows)
- Choose partition keys based on common query patterns
- Ensure proper indexing on each partition
- Monitor partition sizes and query plans regularly