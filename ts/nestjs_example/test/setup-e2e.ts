import { vi } from 'vitest';

// Global test setup for e2e tests
vi.mock('typeorm', async () => {
  const actual = await vi.importActual('typeorm');
  return {
    ...actual,
    // Mock TypeORM for testing if needed
  };
});

// Set test environment variables
process.env.NODE_ENV = 'test';
process.env.DATABASE_HOST = 'localhost';
process.env.DATABASE_PORT = '5433';
process.env.DATABASE_USERNAME = 'test';
process.env.DATABASE_PASSWORD = 'test';
process.env.DATABASE_NAME = 'todo_test';
process.env.REDIS_HOST = 'localhost';
process.env.REDIS_PORT = '6380';