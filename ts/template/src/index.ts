import { serve } from '@hono/node-server'
import { Hono } from 'hono'
import { cors } from 'hono/cors'
import { logger } from 'hono/logger'

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

const port = process.env.PORT ? parseInt(process.env.PORT) : 3000

console.log(`🚀 Server is running on port ${port}`)
console.log(`📱 Health check: http://localhost:${port}/health`)
console.log(`🌐 API endpoint: http://localhost:${port}/api/users`)
console.log(`🏠 Root endpoint: http://localhost:${port}`)

serve({
  fetch: app.fetch,
  port,
})