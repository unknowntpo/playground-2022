"use client";

import { useState } from "react";
import { Todo } from "@/lib/types/database";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { Save, X } from "lucide-react";

interface EditTodoProps {
  todo: Todo;
  onCancel: () => void;
  onSuccess: () => void;
}

export function EditTodo({ todo, onCancel, onSuccess }: EditTodoProps) {
  const [title, setTitle] = useState(todo.title);
  const [description, setDescription] = useState(todo.description || "");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) return;

    setLoading(true);
    try {
      const response = await fetch(`/api/todos/${todo.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: title.trim(),
          description: description.trim() || null,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to update todo');
      }

      onSuccess();
    } catch (error) {
      console.error("Error updating todo:", error);
    }
    setLoading(false);
  };

  return (
    <Card>
      <CardContent className="p-4">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <Input
              placeholder="Todo title..."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              disabled={loading}
              required
              autoFocus
            />
          </div>
          <div>
            <Textarea
              placeholder="Description (optional)..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              disabled={loading}
              rows={3}
            />
          </div>
          <div className="flex gap-2">
            <Button type="submit" disabled={loading || !title.trim()} size="sm">
              <Save className="h-4 w-4 mr-1" />
              {loading ? "Saving..." : "Save"}
            </Button>
            <Button type="button" variant="outline" onClick={onCancel} size="sm">
              <X className="h-4 w-4 mr-1" />
              Cancel
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}