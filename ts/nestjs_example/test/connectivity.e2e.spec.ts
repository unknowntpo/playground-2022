import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { DataSource } from 'typeorm';
import { describe, beforeEach, it, expect, afterEach } from 'vitest';
import { Redis } from 'ioredis';

import configuration from '@/config/configuration';
import { getDatabaseConfig } from '@/config/database.config';
import { Todo } from '@/todos/entities/todo.entity';

describe('Database and Redis Connectivity (e2e)', () => {
  let app: INestApplication;
  let dataSource: DataSource;

  beforeEach(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [
        ConfigModule.forRoot({
          isGlobal: true,
          load: [configuration],
          envFilePath: ['.env.test'],
        }),
        TypeOrmModule.forRootAsync({
          imports: [ConfigModule],
          useFactory: getDatabaseConfig,
          inject: [ConfigService],
        }),
        TypeOrmModule.forFeature([Todo]),
      ],
    }).compile();

    app = moduleFixture.createNestApplication();
    await app.init();

    dataSource = app.get(DataSource);
  });

  afterEach(async () => {
    await app.close();
  });

  describe('Database Connectivity', () => {
    it('should connect to PostgreSQL database successfully', async () => {
      expect(dataSource.isInitialized).toBe(true);
      expect(dataSource.options.type).toBe('postgres');
      expect(dataSource.options.database).toBe('todo_test');
    });

    it('should be able to query the database', async () => {
      const result = await dataSource.query('SELECT 1 as test_value');

      expect(result).toHaveLength(1);
      expect(result[0]).toHaveProperty('test_value', 1);
    });

    it('should have todos table available', async () => {
      const result = await dataSource.query(`
        SELECT table_name 
        FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_name = 'todos'
      `);

      expect(result).toHaveLength(1);
      expect(result[0].table_name).toBe('todos');
    });

    it('should verify todos table structure', async () => {
      const result = await dataSource.query(`
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

      expect(result).toHaveLength(expectedColumns.length);

      expectedColumns.forEach((expectedColumn, index) => {
        expect(result[index]).toMatchObject(expectedColumn);
      });
    });
  });

  describe('Redis Connectivity', () => {
    let redis: Redis;

    beforeAll(() => {
      // Dynamically import Redis to test connection
      redis = new Redis({
        host: configuration().redis.host,
        port: configuration().redis.port,
      });
    });

    afterAll(() => {
      redis.disconnect();
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
  });
});
