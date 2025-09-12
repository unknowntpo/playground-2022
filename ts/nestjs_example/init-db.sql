-- Initialize the database for development
-- This script runs when the PostgreSQL container starts

-- Create additional schemas if needed
CREATE SCHEMA IF NOT EXISTS public;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE todo_dev TO dev;
GRANT ALL ON SCHEMA public TO dev;