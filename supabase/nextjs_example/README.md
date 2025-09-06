# 📋 **Supabase + Next.js Todo App with Clean Architecture**

A production-ready todo application demonstrating clean frontend/backend separation using Next.js 15, Supabase, and modern development practices.

## 🏗️ **Architecture**

```
Frontend (React/Next.js) → REST API → Supabase PostgreSQL
         (fetch calls)      (routes)     (database)
```

**Clean separation**: Frontend never calls Supabase directly - all data flows through custom REST API routes.

## ✅ **Features**

### **Core Functionality**
- ✅ **Full CRUD Operations**: Create, read, update, delete todos
- ✅ **Pagination**: Server-side pagination with metadata
- ✅ **Real-time UI**: Optimistic updates with server sync
- ✅ **Type Safety**: Full TypeScript coverage
- ✅ **Responsive Design**: Mobile-friendly with Tailwind CSS

### **Developer Experience**
- ✅ **API Documentation**: Auto-generated Swagger/OpenAPI docs
- ✅ **Testing**: Vitest with component and API tests
- ✅ **Local Development**: Supabase local stack with Docker
- ✅ **Code Quality**: ESLint, TypeScript, modern practices

## 🚀 **Quick Start**

### **Prerequisites**
- Node.js 18+ 
- pnpm
- Docker (for Supabase local)

### **Installation**
```bash
# Clone and install
git clone <repo-url>
cd nextjs_example
pnpm install

# Start Supabase local stack
supabase start

# Start development server
pnpm dev
```

### **Access Points**
- **App**: http://localhost:3000
- **API Docs**: http://localhost:3000/api-docs  
- **Supabase Studio**: http://localhost:54323

## 📡 **API Endpoints**

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/todos?page=1&limit=10` | List todos with pagination |
| `POST` | `/api/todos` | Create new todo |
| `PATCH` | `/api/todos/{id}` | Update specific todo |
| `DELETE` | `/api/todos/{id}` | Delete specific todo |

### **Example API Usage**

```bash
# Get todos with pagination
curl "http://localhost:3000/api/todos?page=1&limit=5"

# Create new todo
curl -X POST -H "Content-Type: application/json" \
  -d '{"title":"Learn Supabase","description":"Complete tutorial"}' \
  "http://localhost:3000/api/todos"

# Update todo
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"completed":true}' \
  "http://localhost:3000/api/todos/{id}"

# Delete todo  
curl -X DELETE "http://localhost:3000/api/todos/{id}"
```

## 🗄️ **Database Schema**

```sql
CREATE TABLE todos (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  title text NOT NULL,
  description text,
  completed boolean DEFAULT false,
  created_at timestamp DEFAULT now(),
  updated_at timestamp DEFAULT now()
);
```

## 📁 **Project Structure**

```
nextjs_example/
├── app/
│   ├── api/todos/           # REST API routes
│   ├── todos/               # Frontend todo management
│   ├── api-docs/            # Swagger UI page
│   └── layout.tsx           # Root layout
├── components/
│   ├── ui/                  # shadcn/ui components
│   ├── todo-list.tsx        # Todo list display
│   ├── todo-item.tsx        # Individual todo item
│   ├── add-todo.tsx         # Create todo form
│   └── edit-todo.tsx        # Edit todo form
├── lib/
│   ├── supabase/            # Database clients (server/client)
│   ├── types/database.ts    # TypeScript definitions
│   └── utils.ts            # Helper functions
├── supabase/
│   ├── migrations/          # Database schema
│   └── seed.sql            # Sample data
└── __tests__/              # Test files
```

## 🛠️ **Development Commands**

```bash
# Development
pnpm dev                    # Start Next.js dev server
supabase start             # Start local Supabase

# Testing  
pnpm test                  # Run tests in watch mode
pnpm test:run              # Run tests once

# Production
pnpm build                 # Build for production
pnpm start                 # Start production server

# Database
supabase db reset          # Apply migrations + seeds
supabase studio            # Open Supabase Studio
```

## 📚 **Technology Stack**

### **Frontend**
- **Next.js 15**: App Router, Server Components, TypeScript
- **React 19**: Latest React with concurrent features
- **shadcn/ui**: Modern component library
- **Tailwind CSS**: Utility-first styling

### **Backend** 
- **Supabase**: PostgreSQL database with REST API
- **Next.js API Routes**: Custom REST endpoints
- **TypeScript**: Full type safety

### **Developer Tools**
- **Vitest**: Fast unit and integration testing
- **ESLint**: Code quality and consistency
- **Swagger/OpenAPI**: Auto-generated documentation
- **Docker**: Local development environment

## 📖 **API Documentation**

Interactive Swagger documentation is available at `/api-docs` when running the development server.

**Import to Postman**:
1. Open Postman → Import
2. Use URL: `http://localhost:3000/api/docs`  
3. Complete collection will be imported with all endpoints

## 🧪 **Testing**

```bash
# Run all tests
pnpm test:run

# Run tests in watch mode  
pnpm test

# Run with coverage
pnpm test:coverage
```

**Test Coverage**:
- ✅ Component unit tests
- ✅ API endpoint integration tests  
- ✅ Database schema validation
- ✅ TypeScript type checking

## 🚀 **Deployment**

### **Production Setup**
1. **Create Supabase project**: https://database.new
2. **Configure environment**:
   ```bash
   NEXT_PUBLIC_SUPABASE_URL=https://your-project.supabase.co
   NEXT_PUBLIC_SUPABASE_PUBLISHABLE_OR_ANON_KEY=your-anon-key
   ```
3. **Deploy to Vercel**:
   ```bash
   npx vercel --prod
   ```

## 🔄 **Extension Opportunities**

This project serves as a solid foundation for:

### **Immediate Extensions**
- **Authentication**: User-specific todos with JWT
- **Real-time**: WebSocket subscriptions for live updates
- **Advanced Features**: Categories, tags, due dates, file attachments
- **Mobile App**: React Native consuming the same REST API

### **Architecture Benefits**
- ✅ **API-first**: Any client can consume the REST API
- ✅ **Type-safe**: Full TypeScript coverage end-to-end  
- ✅ **Testable**: Clean separation enables comprehensive testing
- ✅ **Scalable**: Easy to add caching, rate limiting, monitoring
- ✅ **Documented**: Self-documenting with OpenAPI/Swagger

### **Enterprise Features**
- Multi-tenancy with organizations
- Advanced permissions and roles
- Audit logging and analytics  
- API versioning and backwards compatibility
- Microservices integration

## 🤝 **Contributing**

This project follows modern development practices:

1. **Code Style**: ESLint + Prettier + TypeScript strict mode
2. **Testing**: All new features require tests
3. **Documentation**: API changes require Swagger annotation updates
4. **Type Safety**: No `any` types, full TypeScript coverage

## 📄 **License**

MIT License - feel free to use this as a foundation for your own projects.

---

## 🎯 **Perfect For**

- **Learning**: Modern full-stack development patterns
- **Prototyping**: Quick MVP development with solid architecture  
- **Enterprise**: Foundation for scalable business applications
- **Mobile Backend**: REST API ready for any client application
- **Team Development**: Clear contracts and separation of concerns

**Status**: ✅ **Production-ready** with comprehensive documentation and testing infrastructure.
