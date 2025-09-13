import {
  Controller,
  Get,
  Post,
  Body,
  Patch,
  Param,
  Delete,
  Query,
  ParseUUIDPipe,
  HttpCode,
  HttpStatus,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiParam,
  ApiQuery,
} from '@nestjs/swagger';
import { TodosService } from '@/todos/todos.service';
import { CreateTodoDto } from '@/todos/dto/create-todo.dto';
import { UpdateTodoDto } from '@/todos/dto/update-todo.dto';
import { PaginationQueryDto } from '@/todos/dto/pagination-query.dto';
import { PaginatedTodosResponseDto } from '@/todos/dto/paginated-todos-response.dto';
import { Todo } from '@/todos/entities/todo.entity';

@ApiTags('todos')
@Controller('todos')
export class TodosController {
  constructor(private readonly todosService: TodosService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new todo' })
  @ApiResponse({
    status: 201,
    description: 'Todo created successfully',
    type: Todo,
  })
  @ApiResponse({
    status: 400,
    description: 'Bad request - validation error',
  })
  async create(@Body() createTodoDto: CreateTodoDto): Promise<Todo> {
    return this.todosService.create(createTodoDto);
  }

  @Get()
  @ApiOperation({ summary: 'Get all todos with pagination' })
  @ApiQuery({
    name: 'page',
    required: false,
    type: Number,
    description: 'Page number for pagination',
    example: 1,
  })
  @ApiQuery({
    name: 'limit',
    required: false,
    type: Number,
    description: 'Number of items per page',
    example: 10,
  })
  @ApiQuery({
    name: 'completed',
    required: false,
    type: Boolean,
    description: 'Filter by completion status',
    example: false,
  })
  @ApiQuery({
    name: 'priority',
    required: false,
    enum: ['low', 'medium', 'high'],
    description: 'Filter by priority level',
    example: 'medium',
  })
  @ApiResponse({
    status: 200,
    description: 'List of todos with pagination metadata',
    type: PaginatedTodosResponseDto,
  })
  async findAll(@Query() query: PaginationQueryDto) {
    return this.todosService.findAll(query);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific todo by ID' })
  @ApiParam({
    name: 'id',
    type: 'string',
    format: 'uuid',
    description: 'Todo ID',
  })
  @ApiResponse({
    status: 200,
    description: 'Todo found',
    type: Todo,
  })
  @ApiResponse({
    status: 404,
    description: 'Todo not found',
  })
  async findOne(@Param('id', ParseUUIDPipe) id: string): Promise<Todo> {
    return this.todosService.findOne(id);
  }

  @Patch(':id')
  @ApiOperation({ summary: 'Update a todo partially' })
  @ApiParam({
    name: 'id',
    type: 'string',
    format: 'uuid',
    description: 'Todo ID',
  })
  @ApiResponse({
    status: 200,
    description: 'Todo updated successfully',
    type: Todo,
  })
  @ApiResponse({
    status: 404,
    description: 'Todo not found',
  })
  @ApiResponse({
    status: 400,
    description: 'Bad request - validation error',
  })
  async update(
    @Param('id', ParseUUIDPipe) id: string,
    @Body() updateTodoDto: UpdateTodoDto,
  ): Promise<Todo> {
    return this.todosService.update(id, updateTodoDto);
  }

  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  @ApiOperation({ summary: 'Delete a todo' })
  @ApiParam({
    name: 'id',
    type: 'string',
    format: 'uuid',
    description: 'Todo ID',
  })
  @ApiResponse({
    status: 204,
    description: 'Todo deleted successfully',
  })
  @ApiResponse({
    status: 404,
    description: 'Todo not found',
  })
  async remove(@Param('id', ParseUUIDPipe) id: string): Promise<void> {
    return this.todosService.remove(id);
  }

  @Get('stats/summary')
  @ApiOperation({ summary: 'Get todo statistics' })
  @ApiResponse({
    status: 200,
    description: 'Todo statistics',
    schema: {
      type: 'object',
      properties: {
        total: { type: 'number', example: 25 },
        completed: { type: 'number', example: 10 },
        pending: { type: 'number', example: 15 },
      },
    },
  })
  async getStats() {
    return this.todosService.getStats();
  }
}