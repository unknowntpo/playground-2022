import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { ConfigService } from '@nestjs/config';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  // Enable validation globally
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      transformOptions: {
        enableImplicitConversion: true,
      },
    }),
  );

  // Setup Swagger
  const config = new DocumentBuilder()
    .setTitle('TODO API')
    .setDescription('NestJS TODO List API with Redis caching')
    .setVersion('1.0')
    .addTag('todos', 'Todo management operations')
    .addTag('health', 'Health check operations')
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup(
    configService.get<string>('swagger.prefix') || 'api/docs',
    app,
    document,
  );

  const port = configService.get<number>('port') || 3000;
  await app.listen(port);
  console.log(`Application is running on: http://localhost:${port}`);
  console.log(
    `Swagger documentation available at: http://localhost:${port}/${configService.get<string>('swagger.prefix') || 'api/docs'}`,
  );
}
bootstrap();
