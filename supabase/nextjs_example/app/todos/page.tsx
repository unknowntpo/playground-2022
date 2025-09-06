import { TodoList } from "@/components/todo-list";
import { AddTodo } from "@/components/add-todo";
import { Todo } from "@/lib/types/database";

interface TodosPageProps {
  searchParams: Promise<{
    page?: string;
    limit?: string;
  }>;
}

export default async function TodosPage({ searchParams }: TodosPageProps) {
  const params = await searchParams;
  const page = params.page || '1';
  const limit = params.limit || '10';

  try {
    const url = `${process.env.NEXT_PUBLIC_APP_URL || 'http://localhost:3000'}/api/todos?page=${page}&limit=${limit}`;
    const response = await fetch(url, {
      cache: 'no-store' // Ensure fresh data on each request
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch todos: ${response.statusText}`);
    }

    const { data: todos, pagination } = await response.json();

    return (
      <div className="container mx-auto max-w-4xl py-8 px-4">
        <h1 className="text-3xl font-bold mb-8">Todo List</h1>
        <div className="space-y-6">
          <AddTodo />
          <TodoList todos={todos as Todo[]} />
          {pagination && (
            <div className="text-sm text-muted-foreground text-center">
              Page {pagination.page} of {pagination.totalPages} 
              ({pagination.total} total todos)
            </div>
          )}
        </div>
      </div>
    );
  } catch (error) {
    console.error("Error fetching todos:", error);
    return <div>Error loading todos</div>;
  }
}