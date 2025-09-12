import { ApiProperty, PartialType } from '@nestjs/swagger';
import { IsOptional, IsBoolean } from 'class-validator';
import { CreateTodoDto } from '@/todos/dto/create-todo.dto';

export class UpdateTodoDto extends PartialType(CreateTodoDto) {
  @ApiProperty({
    description: 'Whether the todo is completed',
    example: true,
    required: false,
  })
  @IsBoolean()
  @IsOptional()
  completed?: boolean;
}