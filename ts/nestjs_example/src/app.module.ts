import { Module } from '@nestjs/common';
import { TodosModule } from '@/todos/todos.module';
import { ConfigModule } from '@nestjs/config';
import configuration from '@/config/configuration';

@Module({
  imports: [TodosModule, ConfigModule.forRoot({ load: [configuration] })],
})
export class AppModule {}
