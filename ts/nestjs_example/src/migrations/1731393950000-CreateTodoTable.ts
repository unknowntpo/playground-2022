import { MigrationInterface, QueryRunner } from 'typeorm';

export class CreateTodoTable1731393950000 implements MigrationInterface {
  name = 'CreateTodoTable1731393950000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TYPE "todos_priority_enum" AS ENUM('low', 'medium', 'high')
        `);

    await queryRunner.query(`
      CREATE TABLE "todos"
      (
        "id"          uuid                   NOT NULL DEFAULT uuid_generate_v4(),
        "title"       character varying(255) NOT NULL,
        "description" text,
        "completed"   boolean                NOT NULL DEFAULT false,
        "priority"    "todos_priority_enum"  NOT NULL DEFAULT 'medium',
        "dueDate"     TIMESTAMP,
        "tags"        text,
        "createdAt"   TIMESTAMP              NOT NULL DEFAULT now(),
        "updatedAt"   TIMESTAMP              NOT NULL DEFAULT now(),
        CONSTRAINT "PK_ca8cafd59ca6faaf67995344225" PRIMARY KEY ("id")
      )
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`DROP TABLE "todos"`);
    await queryRunner.query(`DROP TYPE "todos_priority_enum"`);
  }
}
