# RabbitMQ Demo Application

This application demonstrates real-world RabbitMQ patterns using threaded producers and consumers. Each example simulates a concrete business scenario.

## 🚀 Quick Start

### Running the Application

1. **Start RabbitMQ using Docker Compose:**
   ```bash
   docker-compose up -d
   ```

2. **Run the application:**
   ```bash
   ./gradlew run --args='--help'    # Show available commands
   ./gradlew run --args='ecommerce' # Run e-commerce demo
   ```

3. **Stop RabbitMQ:**
   ```bash
   docker-compose down
   ```

## 📋 Available Commands

### E-commerce Order Processing (`ecommerce` or `ec`)
Simulates Amazon-style order distribution to warehouse workers using RabbitMQ work queues.

```bash
# Basic usage
./gradlew run --args='ecommerce'

# Custom configuration
./gradlew run --args='ecommerce --workers 5 --customers 3 --orders 10'

# Auto mode (runs for 30 seconds automatically)
./gradlew run --args='ecommerce --auto'

# Get help
./gradlew run --args='ecommerce --help'
```

**Options:**
- `-w, --workers`: Number of warehouse workers (default: 3)
- `-c, --customers`: Number of customers placing orders (default: 2)  
- `-o, --orders`: Orders per customer (default: 5)
- `--auto`: Run automatically without user input

## 🧪 Testing

The application includes comprehensive integration tests using Testcontainers with the same docker-compose.yml.

### Running Tests

1. **Make sure Docker is running** (Testcontainers needs it)

2. **Run all tests:**
   ```bash
   ./gradlew test
   ```

3. **Run specific test:**
   ```bash
   ./gradlew test --tests EcommerceServiceTest
   ```

4. **Run tests with verbose output:**
   ```bash
   ./gradlew test --info
   ```

### Test Features

- ✅ **Uses same docker-compose.yml** for both dev and test environments
- ✅ **Automatic container lifecycle** managed by Testcontainers
- ✅ **Real RabbitMQ integration** tests message flow end-to-end
- ✅ **Order distribution verification** ensures round-robin works
- ✅ **Graceful shutdown testing** verifies resource cleanup

### Test Structure

```
app/src/test/java/
└── org/example/
    └── EcommerceServiceTest.java  # Integration tests
```

Tests verify:
- Orders are processed by multiple workers
- Load balancing distributes orders across workers  
- System handles no orders gracefully
- Shutdown process works correctly

## 🐰 RabbitMQ Management UI

When running docker-compose, you can access the RabbitMQ management interface:

- **URL**: http://localhost:15672
- **Username**: admin
- **Password**: password

Use this to monitor:
- Queue depths
- Message rates
- Connection statistics
- Exchange routing

## 🏗️ Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Customer 1 │    │  Customer 2 │    │    ...      │
│ (Producer)  │    │ (Producer)  │    │             │
└─────┬───────┘    └─────┬───────┘    └─────┬───────┘
      │                  │                  │
      └──────────┬───────────────┬──────────┘
                 │               │
                 v               v
         ┌─────────────────────────────┐
         │       RabbitMQ Queue        │
         │       (order_queue)         │
         └─────────────┬───────────────┘
                       │
          ┌────────────┼────────────┐
          │            │            │
          v            v            v
    ┌─────────┐  ┌─────────┐  ┌─────────┐
    │Worker 1 │  │Worker 2 │  │Worker 3 │
    │(Consumer)│  │(Consumer)│  │(Consumer)│
    └─────────┘  └─────────┘  └─────────┘
```

## 📊 Example Output

```
=== 📦 E-commerce Order Processing System ===
Amazon-style order processing using RabbitMQ work queues

Configuration:
  🏭 Warehouse workers: 3
  🛒 Customers: 2  
  📦 Orders per customer: 5

🏭 Starting 3 warehouse workers...
✅ 3 warehouse workers are ready and waiting for orders!

[Customer-1] Sent order: Order{orderId=1, customerId=4523, items=[laptop, mouse, keyboard], totalAmount=1367.43}
[Warehouse-Worker-1] Received order: Order{orderId=1, customerId=4523, items=[laptop, mouse, keyboard], totalAmount=1367.43}
[Warehouse-Worker-1] ✓ Order 1 packaged and ready for shipping!

📊 Final Statistics:
Total orders processed: 10
✅ E-commerce demo complete!
```

## 🛠️ Development

### Project Structure

```
app/src/main/java/org/example/
├── RabbitMQCLI.java              # Main CLI entry point
├── command/
│   └── EcommerceCommand.java     # E-commerce command implementation
├── service/
│   └── EcommerceService.java     # Business logic (testable)
├── producer/
│   └── OrderProducer.java        # Order generation
├── consumer/
│   └── OrderProcessor.java       # Order processing workers  
└── model/
    └── Order.java                # Order data model
```

### Adding New Commands

1. Create new command class in `command/` package
2. Extend from base command patterns
3. Add to `RabbitMQCLI.java` subcommands array
4. Create corresponding service and tests

### Dependencies

- **RabbitMQ Client**: AMQP messaging
- **Jackson**: JSON serialization
- **Picocli**: CLI framework
- **Testcontainers**: Integration testing
- **Awaitility**: Async testing utilities
- **AssertJ**: Fluent assertions