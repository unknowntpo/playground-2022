import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
} from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

export enum TodoPriority {
  LOW = 'low',
  MEDIUM = 'medium',
  HIGH = 'high',
}

@Entity('todos')
export class Todo {
  @ApiProperty({
    description: 'Unique identifier for the todo',
    example: '550e8400-e29b-41d4-a716-446655440000',
  })
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @ApiProperty({
    description: 'Title of the todo',
    example: 'Complete project documentation',
    maxLength: 255,
  })
  @Column({ type: 'text', nullable: false, length: 255 })
  title: string;

  @ApiProperty({
    description: 'Detailed description of the todo',
    example: 'Write comprehensive documentation for the TODO API project',
    maxLength: 1000,
    required: false,
  })
  @Column({ type: 'text', nullable: true })
  description?: string;

  @ApiProperty({
    description: 'Whether the todo is completed',
    example: false,
    default: false,
  })
  @Column({ default: false })
  completed: boolean;

  @ApiProperty({
    description: 'Priority level of the todo',
    enum: TodoPriority,
    example: TodoPriority.MEDIUM,
    default: TodoPriority.MEDIUM,
  })
  @Column({
    type: 'enum',
    enum: TodoPriority,
    default: TodoPriority.MEDIUM,
  })
  priority: TodoPriority;

  @ApiProperty({
    description: 'Due date for the todo',
    example: '2025-01-01T12:00:00.000Z',
    required: false,
    type: 'string',
    format: 'date-time',
  })
  @Column({ type: 'timestamp', nullable: true })
  dueDate?: Date;

  @ApiProperty({
    description: 'Tags associated with the todo',
    example: ['documentation', 'urgent'],
    isArray: true,
    required: false,
  })
  @Column({ type: 'simple-array', nullable: true })
  tags?: string[];

  @ApiProperty({
    description: 'Creation timestamp',
    example: '2024-12-09T10:00:00.000Z',
  })
  @CreateDateColumn()
  createdAt: Date;

  @ApiProperty({
    description: 'Last update timestamp',
    example: '2024-12-09T10:00:00.000Z',
  })
  @UpdateDateColumn()
  updatedAt: Date;
}