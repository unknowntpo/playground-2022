import { DataSource } from 'typeorm';
import { Todo } from '@/todos/entities/todo.entity';

export const todoRepositoryProviders = [
  {
    provide: 'TODO_REPOSITORY',
    useFactory: (dataSource: DataSource) => {
      dataSource.getRepository(Todo);
    },
    inject: ['DATA_SOURCE'],
  },
];
