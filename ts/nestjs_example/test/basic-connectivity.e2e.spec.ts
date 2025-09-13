import { describe, beforeAll, afterAll, it, expect } from 'vitest';
import { Client } from 'pg';
import { createClient } from 'redis';
import { getDatabaseConfig } from '@/config/database.config';

describe('Basic Database and Redis Connectivity (e2e)', () => {
  describe('PostgreSQL Connectivity', () => {
    let client: Client;

    beforeAll(async () => {
      client = new Client({
        host: process.env.DATABASE_HOST || 'localhost',
        port: parseInt(process.env.DATABASE_PORT || '5433'),
        user: process.env.DATABASE_USERNAME || 'test',
        password: process.env.DATABASE_PASSWORD || 'test123',
        database: process.env.DATABASE_NAME || 'todo_test',
      });

      await client.connect();
    });

    afterAll(async () => {
      if (client) {
        await client.end();
      }
    });

    it('should connect to PostgreSQL database successfully', async () => {
      const result = await client.query('SELECT version()');
      expect(result.rows).toHaveLength(1);
      expect(result.rows[0].version).toContain('PostgreSQL');
    });

    it('should be able to query the database', async () => {
      const result = await client.query('SELECT 1 as test_value');

      expect(result.rows).toHaveLength(1);
      expect(result.rows[0].test_value).toBe(1);
    });

    it('should have todos table available', async () => {
      const result = await client.query(`
        SELECT table_name 
        FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_name = 'todos'
      `);

      expect(result.rows).toHaveLength(1);
      expect(result.rows[0].table_name).toBe('todos');
    });

    it('should verify todos table structure', async () => {
      const result = await client.query(`
        SELECT column_name, data_type, is_nullable
        FROM information_schema.columns
        WHERE table_name = 'todos'
        ORDER BY ordinal_position
      `);

      const expectedColumns = [
        { column_name: 'id', data_type: 'uuid', is_nullable: 'NO' },
        {
          column_name: 'title',
          data_type: 'character varying',
          is_nullable: 'NO',
        },
        { column_name: 'description', data_type: 'text', is_nullable: 'YES' },
        { column_name: 'completed', data_type: 'boolean', is_nullable: 'NO' },
        {
          column_name: 'priority',
          data_type: 'USER-DEFINED',
          is_nullable: 'NO',
        },
        {
          column_name: 'dueDate',
          data_type: 'timestamp without time zone',
          is_nullable: 'YES',
        },
        { column_name: 'tags', data_type: 'text', is_nullable: 'YES' },
        {
          column_name: 'createdAt',
          data_type: 'timestamp without time zone',
          is_nullable: 'NO',
        },
        {
          column_name: 'updatedAt',
          data_type: 'timestamp without time zone',
          is_nullable: 'NO',
        },
      ];

      expect(result.rows).toHaveLength(expectedColumns.length);

      expectedColumns.forEach((expectedColumn, index) => {
        expect(result.rows[index]).toMatchObject(expectedColumn);
      });
    });

    it('should be able to insert and query a todo', async () => {
      const insertQuery = `
        INSERT INTO todos (title, description, completed, priority)
        VALUES ($1, $2, $3, $4)
        RETURNING id, title, description, completed, priority
      `;

      const insertResult = await client.query(insertQuery, [
        'Test Todo',
        'Test Description',
        false,
        'medium',
      ]);

      expect(insertResult.rows).toHaveLength(1);
      const insertedTodo = insertResult.rows[0];
      expect(insertedTodo.title).toBe('Test Todo');
      expect(insertedTodo.description).toBe('Test Description');
      expect(insertedTodo.completed).toBe(false);
      expect(insertedTodo.priority).toBe('medium');

      // Clean up
      await client.query('DELETE FROM todos WHERE id = $1', [insertedTodo.id]);
    });
  });

  describe('Redis Connectivity', () => {
    let redis: any;

    beforeAll(async () => {
      redis = createClient({
        url: getDatabaseConfig,
        port: parseInt(process.env.REDIS_PORT || '6380'),
      });

      await redis.connect();
    });

    afterAll(async () => {
      if (redis?.isOpen) {
        await redis.disconnect();
      }
    });

    it('should connect to Redis successfully', async () => {
      expect(redis.isOpen).toBe(true);
    });

    it('should be able to set and get values from Redis', async () => {
      const testKey = 'test:connectivity';
      const testValue = 'redis-connection-test';

      // Set a test value
      await redis.set(testKey, testValue);

      // Get the test value
      const retrievedValue = await redis.get(testKey);

      expect(retrievedValue).toBe(testValue);

      // Clean up
      await redis.del(testKey);
    });

    it('should handle Redis operations with expiration', async () => {
      const testKey = 'test:expiry';
      const testValue = 'test-expiry-value';
      const expirySeconds = 2;

      // Set with expiration
      await redis.setEx(testKey, expirySeconds, testValue);

      // Verify it exists
      const retrievedValue = await redis.get(testKey);
      expect(retrievedValue).toBe(testValue);

      // Check TTL
      const ttl = await redis.ttl(testKey);
      expect(ttl).toBeGreaterThan(0);
      expect(ttl).toBeLessThanOrEqual(expirySeconds);

      // Clean up
      await redis.del(testKey);
    });

    it('should be able to use Redis for caching patterns', async () => {
      const cacheKey = 'cache:test:user:123';
      const cacheData = JSON.stringify({
        id: 123,
        name: 'Test User',
        email: 'test@example.com',
      });

      // Simulate cache miss
      let cachedData = await redis.get(cacheKey);
      expect(cachedData).toBeNull();

      // Set cache
      await redis.setEx(cacheKey, 300, cacheData); // 5 minutes

      // Simulate cache hit
      cachedData = await redis.get(cacheKey);
      expect(cachedData).toBe(cacheData);

      const parsedData = JSON.parse(cachedData);
      expect(parsedData).toEqual({
        id: 123,
        name: 'Test User',
        email: 'test@example.com',
      });

      // Clean up
      await redis.del(cacheKey);
    });
  });
});
