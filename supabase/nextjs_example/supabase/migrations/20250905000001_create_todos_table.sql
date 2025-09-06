-- Create todos table
CREATE TABLE todos (
  id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
  title text NOT NULL,
  description text,
  completed boolean DEFAULT false,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now()
);

-- Enable Row Level Security
ALTER TABLE todos ENABLE ROW LEVEL SECURITY;

-- Create policies for authenticated users
CREATE POLICY "Users can view all todos" ON todos FOR SELECT USING (true);
CREATE POLICY "Users can create todos" ON todos FOR INSERT WITH CHECK (true);
CREATE POLICY "Users can update todos" ON todos FOR UPDATE USING (true);
CREATE POLICY "Users can delete todos" ON todos FOR DELETE USING (true);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_todos_updated_at
    BEFORE UPDATE ON todos
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();

export interface Todo {
    id: string;
    title: string;
    description: string | null;
    completed: boolean;
    created_at: string;
    updated_at: string;
}

