# MyBatis + Caffeine Cache Demo

This project demonstrates the integration of MyBatis with Caffeine Cache for efficient data caching in a JAX-RS REST API application.

## Architecture

- **Entity Layer**: User and Role entities with many-to-many relationships
- **Mapper Layer**: MyBatis mappers with Caffeine cache configuration
- **Service Layer**: Business logic with manual session management
- **Resource Layer**: JAX-RS REST endpoints
- **Database**: MySQL with Docker Compose setup

## Features

- User-Role many-to-many relationship management
- Caffeine cache integration with MyBatis
- Cache invalidation on data modifications
- Performance benchmarks using JMH
- Integration tests for cache behavior
- JAX-RS REST API with Jersey and Jetty

## Prerequisites

- Java 21
- Docker and Docker Compose
- Gradle

## Getting Started

### 1. Start MySQL Database

```bash
docker-compose up -d
```

This will start a MySQL container and automatically initialize the database with tables and sample data.

### 2. Build and Run the Application

```bash
./gradlew run
```

The server will start on port 8080 with the following endpoints:

- `GET /api/roles` - Get all roles
- `GET /api/roles/{id}` - Get role by ID (cached)
- `GET /api/roles/{id}/with-users` - Get role with users (cached)
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID (cached)
- `GET /api/users/{id}/with-roles` - Get user with roles (cached)
- `DELETE /api/users/{id}` - Delete user (invalidates cache)
- `DELETE /api/roles/{roleId}/users/{userId}` - Remove user from role (invalidates cache)

### 3. Run Tests

```bash
./gradlew test
```

### 4. Run Benchmarks

```bash
./gradlew test --tests="*Benchmark*"
```

## Cache Configuration

The Caffeine cache is configured in `RoleMapper.xml` and `UserMapper.xml` with:

- Maximum size: 1000 entries
- Expire after write: 300 seconds
- Expire after access: 60 seconds

## Testing Cache Behavior

1. **Cache Hit Test**: Call `GET /api/roles/1/with-users` multiple times and observe the response time improvement
2. **Cache Invalidation Test**: 
   - Call `GET /api/roles/1/with-users` to cache the data
   - Call `DELETE /api/users/{userId}` to delete a user from the role
   - Call `GET /api/roles/1/with-users` again to see updated results

## Sample Data

The database is initialized with:
- 4 roles: ADMIN, USER, MODERATOR, VIEWER
- 6 users with various role assignments
- Many-to-many relationships demonstrating cache scenarios

## Benchmarks

The JMH benchmarks demonstrate:
- Cached vs non-cached performance
- Cache hit ratios
- Response time improvements with caching enabled

## Project Structure

```
app/src/main/java/org/example/
├── config/
│   └── MyBatisConfig.java          # MyBatis and HikariCP configuration
├── entity/
│   ├── User.java                   # User entity
│   └── Role.java                   # Role entity
├── mapper/
│   ├── UserMapper.java             # User mapper interface
│   └── RoleMapper.java             # Role mapper interface
├── service/
│   ├── UserService.java            # User business logic
│   └── RoleService.java            # Role business logic
├── resource/
│   ├── UserResource.java           # User REST endpoints
│   └── RoleResource.java           # Role REST endpoints
└── App.java                        # Main application class

app/src/main/resources/
├── mapper/
│   ├── UserMapper.xml              # User SQL mappings with cache
│   └── RoleMapper.xml              # Role SQL mappings with cache
├── mybatis-config.xml              # MyBatis configuration
└── application.yml                 # Application configuration

app/src/test/java/org/example/
├── CacheIntegrationTest.java       # Integration tests for cache behavior
└── RoleCacheBenchmark.java         # JMH benchmarks
```