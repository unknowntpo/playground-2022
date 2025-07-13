# RabbitMQ Use Cases Simulation Plan - Real World Examples

This document outlines common RabbitMQ patterns with concrete, real-world examples that are easy to understand for beginners.

## 1. E-commerce Order Processing (Work Queues)
**Real Example**: Amazon-style order processing system
- **Scenario**: Customer places orders → Multiple warehouse workers process them
- **Threads**: 
  - 2 Order Producer threads (simulating customers placing orders)
  - 3 Order Processor threads (simulating warehouse workers)
- **Messages**: `{"orderId": 12345, "customerId": 678, "items": ["laptop", "mouse"], "totalAmount": 1200.00}`
- **Why RabbitMQ**: Distribute order processing load across multiple workers
- **What you'll see**: Orders processed in round-robin fashion among workers

## 2. Twitter Notification System (Fanout Exchange)
**Real Example**: When someone tweets, all their followers get notified
- **Scenario**: User posts a tweet → All followers receive notification
- **Threads**:
  - 1 Tweet Publisher thread (famous user posting tweets)
  - 4 Follower Notification threads (different followers' phones/apps)
- **Messages**: `{"tweetId": 789, "username": "@elonmusk", "content": "Going to Mars!", "timestamp": "2024-01-15T10:30:00Z"}`
- **Why RabbitMQ**: Broadcast same message to millions of followers instantly
- **What you'll see**: Same tweet delivered to all follower notification services

## 3. Netflix Content Recommendation (Direct Exchange)
**Real Example**: Route user actions to specific recommendation engines
- **Scenario**: User watches/likes content → Route to appropriate recommendation system
- **Threads**:
  - 2 User Action producers (watching movies vs TV shows)
  - 3 Recommendation consumers (movie-engine, tv-engine, general-engine)
- **Messages**: `{"userId": 456, "action": "watched", "contentType": "movie", "genreId": "action"}`
- **Routing Keys**: `recommendation.movie`, `recommendation.tv`, `recommendation.general`
- **Why RabbitMQ**: Different recommendation algorithms for different content types
- **What you'll see**: Movie actions go to movie engine, TV actions go to TV engine

## 4. Uber Ride Matching (Topic Exchange)
**Real Example**: Match drivers and riders based on location and car type
- **Scenario**: Ride requests routed based on city, car type, and service level
- **Threads**:
  - 3 Ride Request producers (different cities and car types)
  - 4 Driver consumers (subscribed to specific patterns)
- **Messages**: `{"rideId": 123, "city": "NYC", "carType": "sedan", "serviceLevel": "premium"}`
- **Topic Patterns**: 
  - `ride.NYC.*` (all NYC rides)
  - `ride.*.sedan` (all sedan requests)
  - `ride.NYC.premium` (NYC premium rides)
- **Why RabbitMQ**: Flexible routing based on multiple criteria
- **What you'll see**: Drivers only get rides matching their location/car type

## 5. Banking Transaction Validation (RPC)
**Real Example**: Credit card payment authorization
- **Scenario**: Online store needs to validate payment → Bank responds with approval/denial
- **Threads**:
  - 2 Payment Request threads (online stores)
  - 2 Bank Validation threads (fraud detection + balance check)
- **Messages**: 
  - Request: `{"cardNumber": "****1234", "amount": 99.99, "merchantId": "STORE123"}`
  - Response: `{"approved": true, "transactionId": "TXN789", "reason": "sufficient_funds"}`
- **Why RabbitMQ**: Reliable request-response for critical financial operations
- **What you'll see**: Each payment request gets a specific response

## 6. Hospital Emergency System (Priority Queues)
**Real Example**: Emergency room patient processing
- **Scenario**: Patients arrive with different severity levels → Critical patients treated first
- **Threads**:
  - 2 Patient Arrival threads (normal and emergency cases)
  - 2 Doctor threads (processing patients by priority)
- **Messages**: `{"patientId": 101, "condition": "heart_attack", "priority": 1, "arrivalTime": "14:30"}`
- **Priorities**: 1=Critical, 2=Urgent, 3=Normal
- **Why RabbitMQ**: Life-critical situations must be handled first
- **What you'll see**: Heart attacks processed before routine checkups

## 7. Email Delivery System with Retry (Dead Letter Exchange)
**Real Example**: Gmail-style email delivery with failed message handling
- **Scenario**: Send emails → If delivery fails, retry later → Eventually move to failed folder
- **Threads**:
  - 1 Email Sender thread
  - 2 Email Delivery threads (one simulates failures)
  - 1 Failed Email Handler thread
- **Messages**: `{"emailId": 456, "to": "user@domain.com", "subject": "Welcome!", "retryCount": 0}`
- **Why RabbitMQ**: Handle email delivery failures gracefully
- **What you'll see**: Failed emails automatically retried, then moved to dead letter queue

## 8. Stock Trading Alerts (Message TTL)
**Real Example**: Time-sensitive stock price alerts
- **Scenario**: Stock price changes → Send alerts → Alerts expire if not delivered quickly
- **Threads**:
  - 1 Stock Price Producer (rapid price updates)
  - 1 Slow Mobile App Consumer (simulates poor network)
- **Messages**: `{"symbol": "AAPL", "price": 175.50, "alertType": "price_target", "expiresIn": "30s"}`
- **Why RabbitMQ**: Old price alerts become irrelevant quickly
- **What you'll see**: Old stock alerts automatically deleted if not consumed

## 9. YouTube Video Upload (Publisher Confirms)
**Real Example**: Ensure video upload requests are received
- **Scenario**: Creator uploads video → Confirm upload request received → Process video
- **Threads**:
  - 2 Video Upload producers (content creators)
  - 1 Monitoring thread (tracking successful uploads)
- **Messages**: `{"videoId": "abc123", "title": "My Vlog", "fileSize": "2GB", "uploaderId": "creator456"}`
- **Why RabbitMQ**: Critical to confirm video upload requests aren't lost
- **What you'll see**: Upload only proceeds after broker confirms message received

## 10. Food Delivery Order Tracking (Manual ACK/NACK)
**Real Example**: DoorDash-style order processing with failure recovery
- **Scenario**: Restaurant receives order → Confirms when order ready → Handles preparation failures
- **Threads**:
  - 1 Order Producer (customer orders)
  - 2 Restaurant threads (one succeeds, one occasionally fails)
- **Messages**: `{"orderId": 789, "restaurant": "Pizza Palace", "items": ["margherita", "cola"], "customerAddress": "123 Main St"}`
- **Why RabbitMQ**: Ensure orders aren't lost if restaurant can't fulfill them
- **What you'll see**: Failed orders automatically redelivered to other restaurants

## Implementation Strategy

### Thread Architecture
- **Producer Threads**: Generate and send messages at configurable rates
- **Consumer Threads**: Process messages with simulated work (delays)
- **Monitoring Thread**: Track metrics (throughput, queue lengths, errors)

### Configuration
- Message rates (messages/second)
- Processing delays (simulate work)
- Error rates (simulate failures)
- Queue configurations (durable, auto-delete, etc.)

### Metrics to Track
- Messages sent/received per second
- Queue depths
- Processing latencies
- Error rates
- Connection/channel statistics

### Simulation Controls
- Start/stop individual producers/consumers
- Adjust message rates dynamically
- Inject failures for testing resilience
- Monitor resource usage

## Dependencies Required
- RabbitMQ Java Client
- Threading utilities (ExecutorService, CountDownLatch)
- Metrics collection (Micrometer or custom)
- Configuration management (properties/YAML)