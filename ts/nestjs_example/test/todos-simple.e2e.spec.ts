import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { describe, beforeAll, it, expect, afterAll } from 'vitest';
import request from 'supertest';

import configuration from '@/config/configuration';
import { getDatabaseConfig } from '@/config/database.config';
import { TodosModule } from '@/todos/todos.module';

describe('TodosController Simple (e2e)', () => {
  let app: INestApplication;

  beforeAll(async () => {
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
        TodosModule,
      ],
    }).compile();

    app = moduleFixture.createNestApplication();
    await app.init();
  });

  afterAll(async () => {
    await app.close();
  });

  it('should create a new todo', async () => {
    const createTodoDto = {
      title: 'Test Todo',
      description: 'Test Description',
      priority: 'medium',
    };

    const response = await request(app.getHttpServer())
      .post('/todos')
      .send(createTodoDto)
      .expect(201);

    expect(response.body).toHaveProperty('id');
    expect(response.body.title).toBe(createTodoDto.title);
  });
});
