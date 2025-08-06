# MyBatis Interceptor Example

This project demonstrates how to implement a MyBatis interceptor that automatically logs all INSERT, UPDATE, and DELETE operations to a change_event table for audit purposes.

## Features

- **AuditInterceptor**: Automatically intercepts INSERT, UPDATE, DELETE operations
- **Change Event Logging**: Records timestamp, table name, operation type, and data in JSON format
- **Sample Implementation**: Includes User and Product entities with full CRUD operations
- **Docker Support**: MySQL database setup with docker-compose

## Project Structure

```
├── docker-compose.yml              # MySQL database setup
├── sql/init.sql                   # Database initialization script
├── app/src/main/java/org/example/
│   ├── App.java                   # Main application with test operations
│   ├── interceptor/
│   │   └── AuditInterceptor.java  # MyBatis interceptor implementation
│   ├── model/
│   │   ├── User.java              # User entity
│   │   └── Product.java           # Product entity
│   └── mapper/
│       ├── UserMapper.java        # User data access
│       └── ProductMapper.java     # Product data access
└── app/src/main/resources/
    └── mybatis-config.xml         # MyBatis configuration
```

## Database Schema

### change_event table
- `id`: Auto-increment primary key
- `timestamp`: When the operation occurred
- `table_name`: Name of the affected table
- `operation_type`: INSERT, UPDATE, or DELETE
- `data`: JSON representation of the operation parameters

### Sample tables
- `user`: Users with username, email, first_name, last_name
- `product`: Products with name, description, price, stock_quantity

## How to Run

1. **Start the database**:
   ```bash
   docker-compose up -d
   ```

2. **Build and run the application**:
   ```bash
   ./gradlew run
   ```

3. **Check audit logs**:
   Connect to MySQL and query the `change_event` table to see the audit logs:
   ```sql
   SELECT * FROM change_event ORDER BY timestamp DESC;
   ```

## How the Interceptor Works

The `AuditInterceptor` uses MyBatis's plugin mechanism to intercept all `Executor.update()` calls, which handle INSERT, UPDATE, and DELETE operations. For each intercepted operation:

1. **Extracts operation details**: Gets the SQL command type and parameters
2. **Extracts table name**: Derives table name from the mapper method ID
3. **Serializes data**: Converts operation parameters to JSON format
4. **Logs to database**: Inserts an entry into the `change_event` table

## Key Components

- **@Intercepts annotation**: Defines which MyBatis method to intercept
- **Table name extraction**: Uses mapper naming conventions to determine affected table
- **JSON serialization**: Uses Jackson to convert parameters to JSON
- **Database logging**: Uses the same database connection to avoid transaction issues

## Example Output

When you run the application, you'll see operations being performed and can then query the audit table to see entries like:

```json
{
  "id": 1,
  "timestamp": "2024-01-15 10:30:15",
  "table_name": "user",
  "operation_type": "INSERT",
  "data": "{\"username\":\"test_user\",\"email\":\"test@example.com\",\"firstName\":\"Test\",\"lastName\":\"User\"}"
}
```