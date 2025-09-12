import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Todo } from '@/todos/entities/todo.entity';
import { CreateTodoDto } from '@/todos/dto/create-todo.dto';
import { UpdateTodoDto } from '@/todos/dto/update-todo.dto';
import { PaginationQueryDto } from '@/todos/dto/pagination-query.dto';

@Injectable()
export class TodosService {
  constructor(
    @InjectRepository(Todo)
    private readonly todoRepository: Repository<Todo>,
  ) {}

  async create(createTodoDto: CreateTodoDto): Promise<Todo> {
    const todo = this.todoRepository.create(createTodoDto);
    return this.todoRepository.save(todo);
  }

  async findAll(query: PaginationQueryDto) {
    const { page = 1, limit = 10, completed } = query;
    const offset = (page - 1) * limit;

    const queryBuilder = this.todoRepository.createQueryBuilder('todo');

    if (completed !== undefined) {
      queryBuilder.where('todo.completed = :completed', { completed });
    }

    queryBuilder
      .orderBy('todo.createdAt', 'DESC')
      .skip(offset)
      .take(limit);

    const [data, total] = await queryBuilder.getManyAndCount();

    return {
      data,
      meta: {
        page,
        limit,
        total,
        totalPages: Math.ceil(total / limit),
      },
    };
  }

  async findOne(id: string): Promise<Todo> {
    const todo = await this.todoRepository.findOne({ where: { id } });
    if (!todo) {
      throw new NotFoundException(`Todo with ID ${id} not found`);
    }
    return todo;
  }

  async update(id: string, updateTodoDto: UpdateTodoDto): Promise<Todo> {
    await this.findOne(id); // Check if exists
    await this.todoRepository.update(id, updateTodoDto);
    return this.findOne(id);
  }

  async remove(id: string): Promise<void> {
    const todo = await this.findOne(id); // Check if exists
    await this.todoRepository.remove(todo);
  }

  async getStats() {
    const [total, completed, pending] = await Promise.all([
      this.todoRepository.count(),
      this.todoRepository.count({ where: { completed: true } }),
      this.todoRepository.count({ where: { completed: false } }),
    ]);

    return {
      total,
      completed,
      pending,
    };
  }
}