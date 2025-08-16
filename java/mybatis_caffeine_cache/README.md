# MyBatis + Caffeine Cache Demo

This project demonstrates the integration of MyBatis with Caffeine Cache for efficient data caching in a JAX-RS REST API application.

## Architecture

- **Entity Layer**: User and Role entities with many-to-many relationships
- **Mapper Layer**: MyBatis mappers with Caffeine cache configuration
- **Service Layer**: Business logic with manual session management
- **Controller Layer**: JAX-RS REST endpoints
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
./gradlew jmh
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

### Performance Results

JMH benchmark results comparing cached vs non-cached MyBatis operations:

```
Benchmark                                                  Mode  Cnt    Score     Error  Units
RoleCacheBenchmark.cachedGetRoleByIdDifferent              avgt    5    0.565 ±   0.154  us/op
RoleCacheBenchmark.cachedGetRoleByIdSame                   avgt    5    0.541 ±   0.112  us/op
RoleCacheBenchmark.cachedGetRoleByIdWithUsersDifferent     avgt    5    0.570 ±   0.127  us/op
RoleCacheBenchmark.cachedGetRoleByIdWithUsersSame          avgt    5    0.579 ±   0.146  us/op
RoleCacheBenchmark.nonCachedGetRoleByIdDifferent           avgt    5  402.637 ± 180.168  us/op
RoleCacheBenchmark.nonCachedGetRoleByIdSame                avgt    5  466.518 ± 258.894  us/op
RoleCacheBenchmark.nonCachedGetRoleByIdWithUsersDifferent  avgt    5  482.195 ±  72.193  us/op
RoleCacheBenchmark.nonCachedGetRoleByIdWithUsersSame       avgt    5  487.807 ± 169.932  us/op
```

### Key Performance Metrics

- **Cache Hit Performance**: ~0.55-0.58 μs per operation
- **Database Query Performance**: ~400-488 μs per operation
- **Performance Improvement**: **700-900x faster** with cache enabled
- **Cache Efficiency**: Consistent sub-microsecond response times regardless of query complexity

### Benchmark Analysis

1. **Cached Operations**: All cached queries perform consistently at ~0.5-0.6 μs
2. **Non-Cached Operations**: Database queries take 400-500 μs (hitting MySQL)
3. **Complex Queries**: JOIN queries with users show same cache performance benefits
4. **Query Variations**: Both same-ID and different-ID patterns benefit equally from caching

### Running Benchmarks

```bash
./gradlew jmh
```

The benchmarks demonstrate:
- Cached vs non-cached performance comparison
- Cache hit consistency across different query patterns
- Dramatic response time improvements with Caffeine cache integration
