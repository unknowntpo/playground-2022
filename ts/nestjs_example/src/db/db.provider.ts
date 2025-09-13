import { DataSource } from 'typeorm';
import configuration from '@/config/configuration';

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
        entities: [__dirname + '/../**/*.entity{.ts,.js}'],
        synchronize: true,
      });

      return dataSource.initialize();
    },
  },
];
