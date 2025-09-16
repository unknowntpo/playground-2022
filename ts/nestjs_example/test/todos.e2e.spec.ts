import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { DataSource, Repository } from 'typeorm';
import { describe, beforeEach, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';

import configuration from '@/config/configuration';
import { TodosModule } from '@/todos/todos.module';
import { Todo } from '@/todos/entities/todo.entity.ts';
import { ValidationPipe } from '@nestjs/common';

describe('TodosController (e2e)', () => {
  let app: INestApplication;
  let dataSource: DataSource;
  let todoRepository: Repository<Todo>;

  beforeAll(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [ConfigModule.forRoot({ load: [configuration] }), TodosModule],
    }).compile();

    app = moduleFixture.createNestApplication();

    // Add global validation pipe
    app.useGlobalPipes(
      new ValidationPipe({
        whitelist: true,
        transform: true,
        forbidNonWhitelisted: true,
      }),
    );

    await app.init();

    dataSource = app.get(DataSource);
    todoRepository = dataSource.getRepository(Todo);
  });

  afterAll(async () => {
    await app.close();
  });

  beforeEach(async () => {
    // Clean up todos before each test
    await todoRepository.clear();
  });

  describe('POST /todos', () => {
    it('should create a new todo with valid data', async () => {
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
      expect(response.body.description).toBe(createTodoDto.description);
      expect(response.body.priority).toBe(createTodoDto.priority);
      expect(response.body.completed).toBe(false);
      expect(response.body).toHaveProperty('createdAt');
      expect(response.body).toHaveProperty('updatedAt');

      // Verify it was saved in database
      const savedTodo = await todoRepository.findOne({
        where: { id: response.body.id },
      });
      expect(savedTodo).toBeDefined();
      expect(savedTodo.title).toBe(createTodoDto.title);
    });

    it('should create a todo with optional fields', async () => {
      const createTodoDto = {
        title: 'Todo with optional fields',
        description: 'Description with optional fields',
        priority: 'high',
        dueDate: '2024-12-31T23:59:59.000Z',
        tags: 'urgent,important',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(createTodoDto)
        .expect(201);

      expect(response.body.title).toBe(createTodoDto.title);
      expect(response.body.priority).toBe(createTodoDto.priority);
      expect(response.body.dueDate).toBe(createTodoDto.dueDate);
      expect(response.body.tags).toBe(createTodoDto.tags);
    });

    it('should return 400 when title is missing', async () => {
      const invalidDto = {
        description: 'Missing title',
        priority: 'medium',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain('title should not be empty');
    });

    it('should return 400 when title is empty', async () => {
      const invalidDto = {
        title: '',
        description: 'Empty title',
        priority: 'medium',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain('title should not be empty');
    });

    it('should return 400 when priority is invalid', async () => {
      const invalidDto = {
        title: 'Test Todo',
        description: 'Invalid priority',
        priority: 'urgent', // Should be 'low', 'medium', or 'high'
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain(
        'priority must be one of the following values: low, medium, high',
      );
    });

    it('should return 400 when dueDate is invalid format', async () => {
      const invalidDto = {
        title: 'Test Todo',
        description: 'Invalid date format',
        priority: 'medium',
        dueDate: 'invalid-date-format',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain(
        'dueDate must be a valid ISO 8601 date string',
      );
    });
  });

  describe('GET /todos', () => {
    beforeEach(async () => {
      // Create test todos
      const todos = [
        {
          title: 'Todo 1',
          description: 'First todo',
          priority: 'low',
          completed: false,
        },
        {
          title: 'Todo 2',
          description: 'Second todo',
          priority: 'medium',
          completed: true,
        },
        {
          title: 'Todo 3',
          description: 'Third todo',
          priority: 'high',
          completed: false,
        },
      ];

      for (const todoData of todos) {
        const todo = todoRepository.create(todoData);
        await todoRepository.save(todo);
      }
    });

    it('should return paginated todos with default pagination', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos')
        .expect(200);

      expect(response.body).toHaveProperty('data');
      expect(response.body).toHaveProperty('total');
      expect(response.body).toHaveProperty('page');
      expect(response.body).toHaveProperty('limit');

      expect(response.body.data).toHaveLength(3);
      expect(response.body.total).toBe(3);
      expect(response.body.page).toBe(1);
      expect(response.body.limit).toBe(10);
    });

    it('should return paginated todos with custom pagination', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos?page=1&limit=2')
        .expect(200);

      expect(response.body.data).toHaveLength(2);
      expect(response.body.total).toBe(3);
      expect(response.body.page).toBe(1);
      expect(response.body.limit).toBe(2);
    });

    it('should filter todos by completed status', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos?completed=true')
        .expect(200);

      expect(response.body.data).toHaveLength(1);
      expect(response.body.data[0].completed).toBe(true);
      expect(response.body.data[0].title).toBe('Todo 2');
    });

    it('should filter todos by priority', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos?priority=high')
        .expect(200);

      expect(response.body.data).toHaveLength(1);
      expect(response.body.data[0].priority).toBe('high');
      expect(response.body.data[0].title).toBe('Todo 3');
    });

    it('should return empty array when no todos match filters', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos?completed=true&priority=high')
        .expect(200);

      expect(response.body.data).toHaveLength(0);
      expect(response.body.total).toBe(0);
    });
  });

  describe('GET /todos/:id', () => {
    let createdTodo: any;

    beforeEach(async () => {
      const todoData = {
        title: 'Test Todo for Get',
        description: 'Test Description for Get',
        priority: 'medium',
        completed: false,
      };

      createdTodo = todoRepository.create(todoData);
      await todoRepository.save(createdTodo);
    });

    it('should return a todo by id', async () => {
      const response = await request(app.getHttpServer())
        .get(`/todos/${createdTodo.id}`)
        .expect(200);

      expect(response.body.id).toBe(createdTodo.id);
      expect(response.body.title).toBe(createdTodo.title);
      expect(response.body.description).toBe(createdTodo.description);
      expect(response.body.priority).toBe(createdTodo.priority);
      expect(response.body.completed).toBe(createdTodo.completed);
    });

    it('should return 400 for invalid UUID format', async () => {
      const response = await request(app.getHttpServer())
        .get('/todos/invalid-uuid')
        .expect(400);

      expect(response.body.message).toContain(
        'Validation failed (uuid is expected)',
      );
    });

    it('should return 404 for non-existent todo', async () => {
      const nonExistentId = '123e4567-e89b-12d3-a456-426614174000';

      const response = await request(app.getHttpServer())
        .get(`/todos/${nonExistentId}`)
        .expect(404);

      expect(response.body.message).toBe(
        `Todo with ID ${nonExistentId} not found`,
      );
    });
  });

  describe('PATCH /todos/:id', () => {
    let createdTodo: any;

    beforeEach(async () => {
      const todoData = {
        title: 'Test Todo for Update',
        description: 'Test Description for Update',
        priority: 'medium',
        completed: false,
      };

      createdTodo = todoRepository.create(todoData);
      await todoRepository.save(createdTodo);
    });

    it('should update a todo partially', async () => {
      const updateDto = {
        title: 'Updated Title',
        completed: true,
      };

      const response = await request(app.getHttpServer())
        .patch(`/todos/${createdTodo.id}`)
        .send(updateDto)
        .expect(200);

      expect(response.body.id).toBe(createdTodo.id);
      expect(response.body.title).toBe(updateDto.title);
      expect(response.body.completed).toBe(updateDto.completed);
      expect(response.body.description).toBe(createdTodo.description); // Unchanged
      expect(response.body.priority).toBe(createdTodo.priority); // Unchanged

      // Verify in database
      const updatedTodo = await todoRepository.findOne({
        where: { id: createdTodo.id },
      });
      expect(updatedTodo.title).toBe(updateDto.title);
      expect(updatedTodo.completed).toBe(updateDto.completed);
    });

    it('should update all fields', async () => {
      const updateDto = {
        title: 'Completely Updated Todo',
        description: 'Completely Updated Description',
        priority: 'high',
        completed: true,
        dueDate: '2024-12-31T23:59:59.000Z',
        tags: 'updated,test',
      };

      const response = await request(app.getHttpServer())
        .patch(`/todos/${createdTodo.id}`)
        .send(updateDto)
        .expect(200);

      expect(response.body.title).toBe(updateDto.title);
      expect(response.body.description).toBe(updateDto.description);
      expect(response.body.priority).toBe(updateDto.priority);
      expect(response.body.completed).toBe(updateDto.completed);
      expect(response.body.dueDate).toBe(updateDto.dueDate);
      expect(response.body.tags).toBe(updateDto.tags);
    });

    it('should return 400 for invalid update data', async () => {
      const invalidDto = {
        title: '', // Empty title should be rejected
        priority: 'invalid-priority',
      };

      const response = await request(app.getHttpServer())
        .patch(`/todos/${createdTodo.id}`)
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain('title should not be empty');
    });

    it('should return 400 for invalid UUID format', async () => {
      const updateDto = { title: 'Updated Title' };

      const response = await request(app.getHttpServer())
        .patch('/todos/invalid-uuid')
        .send(updateDto)
        .expect(400);

      expect(response.body.message).toContain(
        'Validation failed (uuid is expected)',
      );
    });

    it('should return 404 for non-existent todo', async () => {
      const nonExistentId = '123e4567-e89b-12d3-a456-426614174000';
      const updateDto = { title: 'Updated Title' };

      const response = await request(app.getHttpServer())
        .patch(`/todos/${nonExistentId}`)
        .send(updateDto)
        .expect(404);

      expect(response.body.message).toBe(
        `Todo with ID ${nonExistentId} not found`,
      );
    });
  });

  describe('DELETE /todos/:id', () => {
    let createdTodo: any;

    beforeEach(async () => {
      const todoData = {
        title: 'Test Todo for Delete',
        description: 'Test Description for Delete',
        priority: 'medium',
        completed: false,
      };

      createdTodo = todoRepository.create(todoData);
      await todoRepository.save(createdTodo);
    });

    it('should delete a todo successfully', async () => {
      await request(app.getHttpServer())
        .delete(`/todos/${createdTodo.id}`)
        .expect(204);

      // Verify it was deleted from database
      const deletedTodo = await todoRepository.findOne({
        where: { id: createdTodo.id },
      });
      expect(deletedTodo).toBeNull();
    });

    it('should return 400 for invalid UUID format', async () => {
      const response = await request(app.getHttpServer())
        .delete('/todos/invalid-uuid')
        .expect(400);

      expect(response.body.message).toContain(
        'Validation failed (uuid is expected)',
      );
    });

    it('should return 404 for non-existent todo', async () => {
      const nonExistentId = '123e4567-e89b-12d3-a456-426614174000';

      const response = await request(app.getHttpServer())
        .delete(`/todos/${nonExistentId}`)
        .expect(404);

      expect(response.body.message).toBe(
        `Todo with ID ${nonExistentId} not found`,
      );
    });
  });

  describe('Validation Edge Cases', () => {
    it('should reject requests with extra fields when using whitelist', async () => {
      const invalidDto = {
        title: 'Valid Title',
        description: 'Valid Description',
        priority: 'medium',
        extraField: 'This should be rejected',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain(
        'property extraField should not exist',
      );
    });

    it('should handle very long title validation', async () => {
      const invalidDto = {
        title: 'a'.repeat(256), // Assuming 255 is max length
        description: 'Valid Description',
        priority: 'medium',
      };

      const response = await request(app.getHttpServer())
        .post('/todos')
        .send(invalidDto)
        .expect(400);

      expect(response.body.message).toContain(
        'title must be shorter than or equal to 255 characters',
      );
    });
  });
});
