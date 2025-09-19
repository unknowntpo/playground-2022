import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { NotFoundException } from '@nestjs/common';
import { describe, beforeEach, it, expect, vi } from 'vitest';

import { TodosService } from '@/todos/todos.service';
import { Todo, TodoPriority } from '@/todos/entities/todo.entity';
import { CreateTodoDto } from '@/todos/dto/create-todo.dto';
import { UpdateTodoDto } from '@/todos/dto/update-todo.dto';
import { PaginationQueryDto } from '@/todos/dto/pagination-query.dto';

const mockTodo: Todo = {
  id: '550e8400-e29b-41d4-a716-446655440000',
  title: 'Test Todo',
  description: 'Test Description',
  completed: false,
  priority: TodoPriority.MEDIUM,
  dueDate: new Date('2025-01-01'),
  tags: ['test', 'unit'],
  createdAt: new Date(),
  updatedAt: new Date(),
};

const mockQueryBuilder = {
  where: vi.fn().mockReturnThis(),
  orderBy: vi.fn().mockReturnThis(),
  skip: vi.fn().mockReturnThis(),
  take: vi.fn().mockReturnThis(),
  getManyAndCount: vi.fn(),
};

const mockTodoRepository = {
  create: vi.fn(),
  save: vi.fn(),
  findOne: vi.fn(),
  find: vi.fn(),
  update: vi.fn(),
  remove: vi.fn(),
  count: vi.fn(),
  createQueryBuilder: vi.fn(() => mockQueryBuilder),
};

describe('TodosService', () => {
  let service: TodosService;
  let repository: Repository<Todo>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        TodosService,
        {
          provide: getRepositoryToken(Todo),
          useValue: mockTodoRepository,
        },
      ],
    }).compile();

    service = module.get<TodosService>(TodosService);
    repository = module.get<Repository<Todo>>(getRepositoryToken(Todo));

    // Reset mocks before each test
    vi.clearAllMocks();
    mockQueryBuilder.getManyAndCount.mockClear();
  });

  describe('create', () => {
    it('should create a new todo', async () => {
      const createTodoDto: CreateTodoDto = {
        title: 'Test Todo',
        description: 'Test Description',
        priority: TodoPriority.HIGH,
        tags: ['test', 'unit'],
      };

      mockTodoRepository.create.mockReturnValue(mockTodo);
      mockTodoRepository.save.mockResolvedValue(mockTodo);

      const result = await service.create(createTodoDto);

      expect(mockTodoRepository.create).toHaveBeenCalledWith(createTodoDto);
      expect(mockTodoRepository.save).toHaveBeenCalledWith(mockTodo);
      expect(result).toEqual(mockTodo);
    });
  });

  describe('findAll', () => {
    it('should return paginated todos', async () => {
      const query: PaginationQueryDto = { page: 1, limit: 10 };

      mockQueryBuilder.getManyAndCount.mockResolvedValue([[mockTodo], 1]);

      const result = await service.findAll(query);

      expect(mockQueryBuilder.orderBy).toHaveBeenCalledWith(
        'todo.createdAt',
        'DESC',
      );
      expect(mockQueryBuilder.skip).toHaveBeenCalledWith(0);
      expect(mockQueryBuilder.take).toHaveBeenCalledWith(10);
      expect(result).toEqual({
        data: [mockTodo],
        meta: {
          page: 1,
          limit: 10,
          total: 1,
          totalPages: 1,
        },
      });
    });

    it('should filter by completion status', async () => {
      const query: PaginationQueryDto = {
        page: 1,
        limit: 10,
        completed: true,
      };

      mockQueryBuilder.getManyAndCount.mockResolvedValue([[], 0]);

      await service.findAll(query);

      expect(mockQueryBuilder.where).toHaveBeenCalledWith(
        'todo.completed = :completed',
        { completed: true },
      );
    });

    it('should handle pagination correctly', async () => {
      const query: PaginationQueryDto = { page: 2, limit: 5 };

      mockQueryBuilder.getManyAndCount.mockResolvedValue([[], 10]);

      const result = await service.findAll(query);

      expect(mockQueryBuilder.skip).toHaveBeenCalledWith(5); // (2-1) * 5
      expect(mockQueryBuilder.take).toHaveBeenCalledWith(5);
      expect(result.meta.totalPages).toBe(2); // Math.ceil(10/5)
    });
  });

  describe('findOne', () => {
    it('should return a todo if found', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';

      mockTodoRepository.findOne.mockResolvedValue(mockTodo);

      const result = await service.findOne(todoId);

      expect(mockTodoRepository.findOne).toHaveBeenCalledWith({
        where: { id: todoId },
      });
      expect(result).toEqual(mockTodo);
    });

    it('should throw NotFoundException if todo not found', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';

      mockTodoRepository.findOne.mockResolvedValue(null);

      await expect(service.findOne(todoId)).rejects.toThrow(NotFoundException);
      expect(mockTodoRepository.findOne).toHaveBeenCalledWith({
        where: { id: todoId },
      });
    });
  });

  describe('update', () => {
    it('should update and return the todo', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';
      const updateTodoDto: UpdateTodoDto = {
        title: 'Updated Title',
        completed: true,
      };

      const updatedTodo = { ...mockTodo, ...updateTodoDto };

      // Mock findOne calls (called twice: once to check existence, once to return updated)
      mockTodoRepository.findOne
        .mockResolvedValueOnce(mockTodo) // First call for existence check
        .mockResolvedValueOnce(updatedTodo); // Second call to return updated todo

      mockTodoRepository.update.mockResolvedValue({ affected: 1 });

      const result = await service.update(todoId, updateTodoDto);

      expect(mockTodoRepository.findOne).toHaveBeenCalledTimes(2);
      expect(mockTodoRepository.update).toHaveBeenCalledWith(
        todoId,
        updateTodoDto,
      );
      expect(result).toEqual(updatedTodo);
    });

    it('should throw NotFoundException if todo to update not found', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';
      const updateTodoDto: UpdateTodoDto = { title: 'Updated Title' };

      mockTodoRepository.findOne.mockResolvedValue(null);

      await expect(service.update(todoId, updateTodoDto)).rejects.toThrow(
        NotFoundException,
      );
      expect(mockTodoRepository.update).not.toHaveBeenCalled();
    });
  });

  describe('remove', () => {
    it('should remove the todo', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';

      mockTodoRepository.findOne.mockResolvedValue(mockTodo);
      mockTodoRepository.remove.mockResolvedValue(mockTodo);

      await service.remove(todoId);

      expect(mockTodoRepository.findOne).toHaveBeenCalledWith({
        where: { id: todoId },
      });
      expect(mockTodoRepository.remove).toHaveBeenCalledWith(mockTodo);
    });

    it('should throw NotFoundException if todo to remove not found', async () => {
      const todoId = '550e8400-e29b-41d4-a716-446655440000';

      mockTodoRepository.findOne.mockResolvedValue(null);

      await expect(service.remove(todoId)).rejects.toThrow(NotFoundException);
      expect(mockTodoRepository.remove).not.toHaveBeenCalled();
    });
  });

  describe('getStats', () => {
    it('should return correct statistics', async () => {
      mockTodoRepository.count
        .mockResolvedValueOnce(25) // total
        .mockResolvedValueOnce(10) // completed
        .mockResolvedValueOnce(15); // pending

      const result = await service.getStats();

      expect(mockTodoRepository.count).toHaveBeenCalledTimes(3);
      expect(mockTodoRepository.count).toHaveBeenNthCalledWith(1);
      expect(mockTodoRepository.count).toHaveBeenNthCalledWith(2, {
        where: { completed: true },
      });
      expect(mockTodoRepository.count).toHaveBeenNthCalledWith(3, {
        where: { completed: false },
      });

      expect(result).toEqual({
        total: 25,
        completed: 10,
        pending: 15,
      });
    });
  });
});
