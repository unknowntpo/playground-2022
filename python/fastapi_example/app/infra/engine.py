from sqlalchemy import create_engine, inspect
from sqlmodel import SQLModel
import os

# PostgreSQL connection parameters
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "postgres")
DB_HOST = os.getenv("DB_HOST", "localhost")
DB_PORT = os.getenv("DB_PORT", "5433")
DB_NAME = os.getenv("DB_NAME", "app_db")

# Create PostgreSQL connection URL
postgres_url = f"postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"

# Create engine
engine = create_engine(postgres_url)


def create_db_and_tables():
    print("Database tables:")
    for table in SQLModel.metadata.tables.keys():
        print(f"- {table}")
    SQLModel.metadata.create_all(engine)
