import { TypeOrmModuleOptions } from '@nestjs/typeorm';
import { ConfigService } from '@nestjs/config';
import { Todo } from '@/todos/entities/todo.entity';

export const getDatabaseConfig = (
  configService: ConfigService,
): TypeOrmModuleOptions => ({
  type: 'postgres',
  host: configService.get<string>('database.host'),
  port: configService.get<number>('database.port'),
  username: configService.get<string>('database.username'),
  password: configService.get<string>('database.password'),
  database: configService.get<string>('database.database'),
  entities: [Todo],
  synchronize: false, // Always false in production - use migrations instead
  logging: configService.get<string>('nodeEnv') === 'development',
  autoLoadEntities: false, // Disable auto-loading to prevent ESM conflicts
});
