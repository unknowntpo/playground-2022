import { createSwaggerSpec } from 'next-swagger-doc';
import { NextResponse } from 'next/server';

const spec = createSwaggerSpec({
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'Todo API',
      version: '1.0.0',
      description: 'A clean REST API for managing todos with pagination support',
    },
    servers: [
      {
        url: 'http://localhost:3000',
        description: 'Development server',
      },
    ],
    components: {
      schemas: {
        Todo: {
          type: 'object',
          required: ['id', 'title', 'completed', 'created_at', 'updated_at'],
          properties: {
            id: {
              type: 'string',
              format: 'uuid',
              description: 'Unique identifier for the todo'
            },
            title: {
              type: 'string',
              description: 'The todo title'
            },
            description: {
              type: 'string',
              nullable: true,
              description: 'Optional todo description'
            },
            completed: {
              type: 'boolean',
              description: 'Whether the todo is completed'
            },
            created_at: {
              type: 'string',
              format: 'date-time',
              description: 'When the todo was created'
            },
            updated_at: {
              type: 'string',
              format: 'date-time',
              description: 'When the todo was last updated'
            }
          }
        },
        CreateTodo: {
          type: 'object',
          required: ['title'],
          properties: {
            title: {
              type: 'string',
              description: 'The todo title'
            },
            description: {
              type: 'string',
              description: 'Optional todo description'
            },
            completed: {
              type: 'boolean',
              default: false,
              description: 'Whether the todo is completed'
            }
          }
        },
        UpdateTodo: {
          type: 'object',
          properties: {
            title: {
              type: 'string',
              description: 'The todo title'
            },
            description: {
              type: 'string',
              nullable: true,
              description: 'Optional todo description'
            },
            completed: {
              type: 'boolean',
              description: 'Whether the todo is completed'
            }
          }
        },
        Pagination: {
          type: 'object',
          properties: {
            page: {
              type: 'integer',
              description: 'Current page number'
            },
            limit: {
              type: 'integer', 
              description: 'Items per page'
            },
            total: {
              type: 'integer',
              description: 'Total number of items'
            },
            totalPages: {
              type: 'integer',
              description: 'Total number of pages'
            },
            hasNext: {
              type: 'boolean',
              description: 'Whether there is a next page'
            },
            hasPrev: {
              type: 'boolean',
              description: 'Whether there is a previous page'
            }
          }
        }
      }
    }
  },
  apiFolder: 'app/api',
});

export function GET() {
  return NextResponse.json(spec);
}