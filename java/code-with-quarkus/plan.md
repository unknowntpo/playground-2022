# Code with Quarkus

A demo project shows quarkus' simplicity over Spring Boot

## Demo features

- [ ] Build a simple CRUD app with PostgreSQL, and Redis as a cache.
  - Follows clean architecture
  - unit tests, integration tests for each API should be written

## Implementation Plan

### Current State
- Basic Quarkus starter (Gradle, Java 17)
- Single `/hello` REST endpoint
- Dependencies: quarkus-arc, quarkus-rest, quarkus-junit5, rest-assured

### Test Isolation Strategy
- **Docker Compose**: Single docker-compose.yml for PostgreSQL + Redis (shared dev + test)
- **Cleanup**: @BeforeEach truncate all tables + FLUSHDB Redis before each test
- **No Testcontainers**: Tests use same Docker Compose services
- **Test fails fast**: If compose not running, tests fail immediately

### Implementation Steps

#### 1. Infrastructure Setup
- [ ] Create docker-compose.yml with PostgreSQL and Redis
  - PostgreSQL: port 5432, database `quarkus_demo`
  - Redis: port 6379
  - Volume mounts for data persistence

#### 2. Dependencies
- [ ] Add to build.gradle:
  - quarkus-hibernate-orm-panache
  - quarkus-jdbc-postgresql
  - quarkus-redis-cache
  - quarkus-cache

#### 3. Configuration
- [ ] Configure src/main/resources/application.properties:
  - PostgreSQL datasource (jdbc:postgresql://localhost:5432/quarkus_demo)
  - Redis connection (localhost:6379)
  - Hibernate DDL (update)
- [ ] Configure src/test/resources/application.properties:
  - Same PostgreSQL/Redis endpoints
  - Test-specific settings if needed

#### 4. Domain Layer (Clean Architecture)
- [ ] Create domain entities:
  - Example: Product entity with JPA annotations
  - Business logic in domain if needed
- [ ] Follow clean architecture principles

#### 5. Repository Layer
- [ ] Create repository interfaces (port)
- [ ] Implement Panache repositories (adapter)
  - ProductRepository extends PanacheRepository

#### 6. Service/Use Case Layer
- [ ] Create service interfaces
- [ ] Implement business logic
- [ ] Add @ApplicationScoped

#### 7. REST API Layer
- [ ] Create REST resources (CRUD endpoints)
  - POST /api/products
  - GET /api/products
  - GET /api/products/{id}
  - PUT /api/products/{id}
  - DELETE /api/products/{id}

#### 8. Caching
- [ ] Add @CacheResult on read operations
- [ ] Add @CacheInvalidate on write/update/delete operations
- [ ] Use Redis as cache backend

#### 9. Test Base Class
- [ ] Create AbstractIntegrationTest base class
- [ ] @BeforeEach: Truncate all tables + FLUSHDB Redis
- [ ] Utility methods for test data setup

#### 10. Domain Unit Tests
- [ ] Test entity validation
- [ ] Test domain business logic
- [ ] Pure unit tests (no DB)

#### 11. Service Unit Tests
- [ ] Test service layer with mocked repositories
- [ ] Test business logic isolation
- [ ] Use @InjectMock for dependencies

#### 12. Integration Tests
- [ ] Test REST endpoints end-to-end
- [ ] Verify DB persistence
- [ ] Verify cache hit/miss
- [ ] Each test extends AbstractIntegrationTest
- [ ] Cleanup verified between tests

#### 13. Verification
- [ ] Start Docker Compose: `docker-compose up -d`
- [ ] Run build: `./gradlew clean build`
- [ ] Verify all tests pass with isolation
- [ ] Manual API testing if needed

### Architecture Layers

```
┌─────────────────────────────────────┐
│   REST API (Resources)              │  ← Interface Adapters
├─────────────────────────────────────┤
│   Services/Use Cases                │  ← Application Business Logic
├─────────────────────────────────────┤
│   Domain Entities                   │  ← Enterprise Business Rules
├─────────────────────────────────────┤
│   Repository Interfaces (Ports)     │  ← Interface Adapters
├─────────────────────────────────────┤
│   Repository Impl (Panache)         │  ← Frameworks & Drivers
│   PostgreSQL + Redis                │
└─────────────────────────────────────┘
```