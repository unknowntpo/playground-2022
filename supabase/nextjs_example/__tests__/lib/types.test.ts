import { Todo, CreateTodo, UpdateTodo } from '@/lib/types/database'

describe('Database Types', () => {
  it('should define proper Todo interface', () => {
    const todo: Todo = {
      id: '123',
      title: 'Test Todo',
      description: 'Test description',
      completed: false,
      created_at: '2024-01-01T00:00:00Z',
      updated_at: '2024-01-01T00:00:00Z',
    }

    expect(todo).toBeDefined()
    expect(typeof todo.id).toBe('string')
    expect(typeof todo.title).toBe('string')
    expect(typeof todo.completed).toBe('boolean')
  })

  it('should allow CreateTodo with minimal fields', () => {
    const newTodo: CreateTodo = {
      title: 'New Todo',
    }

    expect(newTodo).toBeDefined()
    expect(newTodo.title).toBe('New Todo')
  })

  it('should allow UpdateTodo with partial fields', () => {
    const update: UpdateTodo = {
      completed: true,
    }

    expect(update).toBeDefined()
    expect(update.completed).toBe(true)
  })
})