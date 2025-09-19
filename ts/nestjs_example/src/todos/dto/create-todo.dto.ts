import { ApiProperty } from '@nestjs/swagger';
import {
  IsString,
  IsNotEmpty,
  MaxLength,
  IsOptional,
  IsEnum,
  IsDateString,
  IsArray,
  ArrayMaxSize,
} from 'class-validator';
import { TodoPriority } from '@/todos/entities/todo.entity';

export class CreateTodoDto {
  @ApiProperty({
    description: 'Title of the todo',
    example: 'Complete project documentation',
    minLength: 1,
    maxLength: 255,
  })
  @IsString()
  @IsNotEmpty()
  @MaxLength(255)
  title: string;

  @ApiProperty({
    description: 'Detailed description of the todo',
    example: 'Write comprehensive documentation for the TODO API project',
    maxLength: 1000,
    required: false,
  })
  @IsString()
  @IsOptional()
  @MaxLength(1000)
  description?: string;

  @ApiProperty({
    description: 'Priority level of the todo',
    enum: TodoPriority,
    example: TodoPriority.MEDIUM,
    default: TodoPriority.MEDIUM,
    required: false,
  })
  @IsEnum(TodoPriority)
  @IsOptional()
  priority?: TodoPriority;

  @ApiProperty({
    description: 'Due date for the todo',
    example: '2025-01-01T12:00:00.000Z',
    type: 'string',
    format: 'date-time',
    required: false,
  })
  @IsDateString()
  @IsOptional()
  dueDate?: string;

  @ApiProperty({
    description: 'Tags associated with the todo',
    example: ['documentation', 'urgent'],
    type: [String],
    maxItems: 10,
    required: false,
  })
  @IsArray()
  @IsString({ each: true })
  @ArrayMaxSize(10)
  @IsOptional()
  tags?: string[];
}
