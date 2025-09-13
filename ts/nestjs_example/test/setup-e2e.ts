// Setup for e2e tests - only runs when e2e tests are executed

// Set test environment variables for e2e tests
process.env.NODE_ENV = 'test';
process.env.DATABASE_HOST = 'localhost';
process.env.DATABASE_PORT = '5433';
process.env.DATABASE_USERNAME = 'test';
process.env.DATABASE_PASSWORD = 'test123';
process.env.DATABASE_NAME = 'todo_test';
process.env.REDIS_HOST = 'localhost';
process.env.REDIS_PORT = '6380';