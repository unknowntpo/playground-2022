# jCasbin Demo

A demonstration of jCasbin framework for authorization with MySQL database persistence using JAX-RS and JDBC.

## Architecture

```
JAX-RS Service <-> jCasbin Enforcer <-> JDBC Adapter <-> MySQL Database
```

## Features

- **Authorization API**: REST endpoints for checking permissions
- **Policy Management**: CRUD operations for policies and roles
- **Database Persistence**: Policies stored in MySQL with JDBC adapter
- **Multi-instance Support**: Policy synchronization across application instances
- **RBAC Model**: Role-based access control with inheritance

## API Endpoints

### Authorization
- `POST /api/auth/check` - Check if subject can perform action on object

### Policy Management
- `GET /api/policies` - Get all policies and roles
- `POST /api/policies` - Add new policy
- `DELETE /api/policies` - Remove policy
- `POST /api/policies/roles` - Assign role to user
- `DELETE /api/policies/roles` - Remove role from user
- `POST /api/policies/reload` - Reload policies from database

## Setup

1. **Database Setup**:
   ```sql
   CREATE DATABASE jcasbin_demo;
   ```

2. **Configuration**: Update `application.properties` with your database credentials

3. **Run Application**:
   ```bash
   ./gradlew run
   ```

## Testing

Run tests with:
```bash
./gradlew test
```

Integration tests use Testcontainers to spin up MySQL for testing policy persistence and multi-instance synchronization.

## Example Usage

### Check Authorization
```bash
curl -X POST http://localhost:8080/api/auth/check \
  -H "Content-Type: application/json" \
  -d '{"subject": "alice", "object": "data1", "action": "read"}'
```

### Add Policy
```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -d '{"subject": "alice", "object": "data1", "action": "read"}'
```

### Assign Role
```bash
curl -X POST "http://localhost:8080/api/policies/roles?user=alice&role=admin"
```