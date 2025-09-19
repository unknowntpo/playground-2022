import { DataSource } from 'typeorm';
import configuration from '@/config/configuration';
import { Todo } from '@/todos/entities/todo.entity';

/**
 * Ref:
 * https://docs.nestjs.com/recipes/sql-typeorm
 */
export const databaseProviders = [
  {
    provide: 'DATA_SOURCE',
    useFactory: async () => {
      const dataSource = new DataSource({
        type: 'postgres',
        host: configuration().database.host || 'localhost',
        port: configuration().database.port || 5432,
        username: configuration().database.username || 'dev',
        password: configuration().database.password || 'dev123',
        database: configuration().database.database || 'todos',
        entities: [Todo],
        synchronize: true,
      });

      return dataSource.initialize();
    },
  },
];
