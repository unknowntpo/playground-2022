export interface Todo {
  id: string;
  title: string;
  description: string | null;
  completed: boolean;
  created_at: string;
  updated_at: string;
}

export interface CreateTodo {
  title: string;
  description?: string;
  completed?: boolean;
}

export interface UpdateTodo {
  title?: string;
  description?: string;
  completed?: boolean;
}

export type Database = {
  public: {
    Tables: {
      todos: {
        Row: Todo;
        Insert: CreateTodo;
        Update: UpdateTodo;
      };
    };
  };
};