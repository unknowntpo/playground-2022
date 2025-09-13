import { Module } from '@nestjs/common';
import { TodosService } from '@/todos/todos.service';
import { TodosController } from '@/todos/todos.controller';
import { todoRepositoryProviders } from '@/todos/todos.repo.provider';
import { DatabaseModule } from '@/db/databaseModule';

@Module({
  imports: [DatabaseModule],
  controllers: [TodosController],
  providers: [...todoRepositoryProviders, TodosService],
  exports: [TodosService],
})
export class TodosModule {}
