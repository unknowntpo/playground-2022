"use client";

import { useState } from "react";
import { Todo } from "@/lib/types/database";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Card, CardContent } from "@/components/ui/card";
import { Trash2, Edit } from "lucide-react";
import { EditTodo } from "./edit-todo";
import { useRouter } from "next/navigation";

interface TodoItemProps {
  todo: Todo;
}

export function TodoItem({ todo }: TodoItemProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleToggleComplete = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/todos/${todo.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ completed: !todo.completed }),
      });

      if (!response.ok) {
        throw new Error('Failed to update todo');
      }

      router.refresh();
    } catch (error) {
      console.error("Error toggling todo:", error);
    }
    setLoading(false);
  };

  const handleDelete = async () => {
    if (!confirm("Are you sure you want to delete this todo?")) return;
    
    setLoading(true);
    try {
      const response = await fetch(`/api/todos/${todo.id}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Failed to delete todo');
      }

      router.refresh();
    } catch (error) {
      console.error("Error deleting todo:", error);
    }
    setLoading(false);
  };

  const handleEditSuccess = () => {
    setIsEditing(false);
    router.refresh();
  };

  if (isEditing) {
    return (
      <EditTodo 
        todo={todo} 
        onCancel={() => setIsEditing(false)}
        onSuccess={handleEditSuccess}
      />
    );
  }

  return (
    <Card className={`w-full ${todo.completed ? "opacity-70" : ""}`}>
      <CardContent className="p-4">
        <div className="flex items-start gap-3">
          <Checkbox
            checked={todo.completed}
            onCheckedChange={handleToggleComplete}
            disabled={loading}
            className="mt-1"
          />
          <div className="flex-1 min-w-0">
            <h3 className={`font-medium ${todo.completed ? "line-through text-muted-foreground" : ""}`}>
              {todo.title}
            </h3>
            {todo.description && (
              <p className={`text-sm mt-1 ${todo.completed ? "line-through text-muted-foreground" : "text-muted-foreground"}`}>
                {todo.description}
              </p>
            )}
            <p className="text-xs text-muted-foreground mt-2">
              Created: {new Date(todo.created_at).toLocaleDateString()}
            </p>
          </div>
          <div className="flex gap-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setIsEditing(true)}
              disabled={loading}
            >
              <Edit className="h-4 w-4" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={handleDelete}
              disabled={loading}
              className="text-red-600 hover:text-red-800"
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}