# Mybatis + Caffeine Cache

This repo demos using MyBatis + Caffeine Cache

https://mybatis.org/caffeine-cache/

## Requirement

We have these relations:

- User
- Role

- User:Role = N:N
User may belongs to multiple roles, and roles may contain multiple users.

Please write tests and benchmarks to make sure that:

- Context multiple users belongs to a role:
    - When getRoleById is executed
    - Correct Roles answer should be returned 
    - When delete a user in this role
    - Users in roles should be updated 

Benchmark:
- Should use benchmark to let us know data is cached.

## Notes:

Should use docker-compose.yml to start MySql service for testing, don't use Testcontainers.

Should use java.ws.rs, don't use Spring framework.



