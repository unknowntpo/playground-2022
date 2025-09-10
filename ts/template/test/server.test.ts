import { describe, it, expect } from 'vitest'
import { Hono } from 'hono'
import { cors } from 'hono/cors'
import { logger } from 'hono/logger'

// Create a test app that mirrors our main app
const createTestApp = () => {
  const app = new Hono()

  // Middleware
  app.use('*', logger())
  app.use('*', cors())

  // Health check endpoint
  app.get('/health', (c) => {
    return c.json({ 
      status: 'ok',
      timestamp: new Date().toISOString(),
      message: 'Server is running!' 
    })
  })

  // Users API endpoints
  app.get('/api/users', (c) => {
    const users = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
      { id: 3, name: 'Bob Johnson', email: 'bob@example.com' },
    ]
    return c.json({ users })
  })

  app.post('/api/users', async (c) => {
    const body = await c.req.json()
    const newUser = {
      id: Date.now(),
      name: body.name,
      email: body.email,
      createdAt: new Date().toISOString()
    }
    return c.json({ user: newUser }, 201)
  })

  // Root endpoint
  app.get('/', (c) => {
    return c.json({
      message: 'Welcome to TypeScript + Hono.js Backend Template!',
      endpoints: {
        health: '/health',
        users: '/api/users'
      }
    })
  })

  return app
}

describe('Backend API', () => {
  const app = createTestApp()

  it('GET / - should return welcome message with endpoints', async () => {
    const res = await app.request('/')
    const data = await res.json()
    
    expect(res.status).toBe(200)
    expect(data.message).toBe('Welcome to TypeScript + Hono.js Backend Template!')
    expect(data.endpoints).toBeDefined()
    expect(data.endpoints.health).toBe('/health')
    expect(data.endpoints.users).toBe('/api/users')
  })

  it('GET /health - should return health status', async () => {
    const res = await app.request('/health')
    const data = await res.json()
    
    expect(res.status).toBe(200)
    expect(data.status).toBe('ok')
    expect(data.message).toBe('Server is running!')
    expect(data.timestamp).toBeDefined()
  })

  it('GET /api/users - should return users list', async () => {
    const res = await app.request('/api/users')
    const data = await res.json()
    
    expect(res.status).toBe(200)
    expect(data.users).toBeDefined()
    expect(Array.isArray(data.users)).toBe(true)
    expect(data.users.length).toBe(3)
    expect(data.users[0]).toHaveProperty('id')
    expect(data.users[0]).toHaveProperty('name')
    expect(data.users[0]).toHaveProperty('email')
  })

  it('POST /api/users - should create a new user', async () => {
    const newUser = {
      name: 'Test User',
      email: 'test@example.com'
    }

    const res = await app.request('/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newUser),
    })

    const data = await res.json()
    
    expect(res.status).toBe(201)
    expect(data.user).toBeDefined()
    expect(data.user.name).toBe(newUser.name)
    expect(data.user.email).toBe(newUser.email)
    expect(data.user.id).toBeDefined()
    expect(data.user.createdAt).toBeDefined()
  })

  it('POST /api/users - should handle malformed request', async () => {
    const res = await app.request('/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: '{"invalid": json}',
    })

    // Hono will return 400 for malformed JSON
    expect(res.status).toBe(400)
  })

  it('should handle 404 for unknown routes', async () => {
    const res = await app.request('/unknown-route')
    
    expect(res.status).toBe(404)
  })
})