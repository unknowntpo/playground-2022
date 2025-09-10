# TypeScript Backend Template

A clean TypeScript backend template with Node.js 22, Hono.js, and Vitest testing.

## Features

- 🚀 **Backend**: Hono.js server with Node.js 22
- 🧪 **Testing**: Vitest for backend testing
- 📦 **Build**: TypeScript compiler
- 🔧 **Development**: Hot reload with tsx watch
- 🎯 **Modern**: ESModules, latest TypeScript features
- 🛠️ **Developer Experience**: ESLint, Prettier, strict TypeScript

## Quick Start

### 1. Install Dependencies
```bash
pnpm install
```

### 2. Development Mode
```bash
pnpm dev
```
This starts the backend server on port 3000 with hot reload.

### 3. Testing
```bash
# Run tests
pnpm test

# Run tests with UI
pnpm test:ui

# Run tests once
pnpm test:run
```

### 4. Build for Production
```bash
pnpm build
```

### 5. Start Production Server
```bash
pnpm start
```

## Project Structure

```
src/
└── index.ts          # Main server file
test/
└── server.test.ts    # API endpoint tests
dist/                 # Build output (generated)
```

## API Endpoints

- `GET /` - Welcome message with available endpoints
- `GET /health` - Health check
- `GET /api/users` - Get all users  
- `POST /api/users` - Create new user

## Scripts

- `pnpm dev` - Start development server with hot reload
- `pnpm build` - Build for production
- `pnpm start` - Start production server
- `pnpm test` - Run tests in watch mode
- `pnpm test:ui` - Run tests with UI
- `pnpm test:run` - Run tests once
- `pnpm typecheck` - Run TypeScript type checking
- `pnpm lint` - Run ESLint
- `pnpm format` - Format code with Prettier

## Technologies Used

- **TypeScript** - Type-safe JavaScript
- **Hono.js** - Fast, lightweight web framework
- **Vitest** - Modern test runner
- **Node.js 22** - Runtime environment
- **tsx** - TypeScript execution and watch mode
- **ESLint** - Code linting
- **Prettier** - Code formatting

## Getting Started with Your Own Project

1. Clone or download this template
2. Update `package.json` with your project details
3. Install dependencies: `pnpm install`
4. Start developing: `pnpm dev`
5. Build for production: `pnpm build`

## Development Tips

- The server runs on port 3000 by default (configurable via PORT env var)
- Health check endpoint is available at `/health`
- API routes are prefixed with `/api`
- Tests are configured to run in Node.js environment
- Hot reload is enabled during development with tsx watch
- Use `curl http://localhost:3000/health` to test the server