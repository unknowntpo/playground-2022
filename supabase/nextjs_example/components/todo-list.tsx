"use client";

import { TodoItem } from "./todo-item";
import { Todo } from "@/lib/types/database";

interface TodoListProps {
  todos: Todo[];
}

export function TodoList({ todos }: TodoListProps) {
  const completedTodos = todos.filter(todo => todo.completed);
  const incompleteTodos = todos.filter(todo => !todo.completed);

  return (
    <div className="space-y-6">
      {incompleteTodos.length > 0 && (
        <div>
          <h2 className="text-xl font-semibold mb-3">Active Todos ({incompleteTodos.length})</h2>
          <div className="space-y-2">
            {incompleteTodos.map((todo) => (
              <TodoItem key={todo.id} todo={todo} />
            ))}
          </div>
        </div>
      )}
      
      {completedTodos.length > 0 && (
        <div>
          <h2 className="text-xl font-semibold mb-3">Completed ({completedTodos.length})</h2>
          <div className="space-y-2">
            {completedTodos.map((todo) => (
              <TodoItem key={todo.id} todo={todo} />
            ))}
          </div>
        </div>
      )}
      
      {todos.length === 0 && (
        <div className="text-center py-8 text-muted-foreground">
          No todos yet. Add one to get started!
        </div>
      )}
    </div>
  );
}