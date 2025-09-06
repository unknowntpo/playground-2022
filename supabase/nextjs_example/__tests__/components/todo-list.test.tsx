import { render, screen } from '@testing-library/react'
import { TodoList } from '@/components/todo-list'
import { Todo } from '@/lib/types/database'

// Mock next/navigation
vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
    refresh: vi.fn(),
  }),
}))

// Mock supabase client
vi.mock('@/lib/supabase/client', () => ({
  createClient: () => ({
    from: vi.fn().mockReturnValue({
      update: vi.fn().mockReturnValue({
        eq: vi.fn().mockResolvedValue({ error: null }),
      }),
      delete: vi.fn().mockReturnValue({
        eq: vi.fn().mockResolvedValue({ error: null }),
      }),
    }),
  }),
}))

const mockTodos: Todo[] = [
  {
    id: '1',
    title: 'Test Todo 1',
    description: 'Test description',
    completed: false,
    created_at: '2024-01-01T00:00:00Z',
    updated_at: '2024-01-01T00:00:00Z',
  },
  {
    id: '2',
    title: 'Test Todo 2',
    description: null,
    completed: true,
    created_at: '2024-01-02T00:00:00Z',
    updated_at: '2024-01-02T00:00:00Z',
  },
]

describe('TodoList', () => {
  it('renders empty state when no todos', () => {
    render(<TodoList todos={[]} />)
    expect(screen.getByText('No todos yet. Add one to get started!')).toBeInTheDocument()
  })

  it('renders active and completed todos separately', () => {
    render(<TodoList todos={mockTodos} />)
    
    expect(screen.getByText('Active Todos (1)')).toBeInTheDocument()
    expect(screen.getByText('Completed (1)')).toBeInTheDocument()
    expect(screen.getByText('Test Todo 1')).toBeInTheDocument()
    expect(screen.getByText('Test Todo 2')).toBeInTheDocument()
  })

  it('displays todo descriptions when available', () => {
    render(<TodoList todos={mockTodos} />)
    expect(screen.getByText('Test description')).toBeInTheDocument()
  })
})