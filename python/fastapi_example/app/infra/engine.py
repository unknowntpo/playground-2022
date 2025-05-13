from sqlalchemy import create_engine, inspect
from sqlmodel import SQLModel


sqlite_file_name = "database.db"
sqlite_url = f"sqlite:///../{sqlite_file_name}"

connect_args = {"check_same_thread": False}
engine = create_engine(sqlite_url, connect_args=connect_args)

def create_db_and_tables():
    print("Database tables:")
    for table in SQLModel.metadata.tables.keys():
        print(f"- {table}")
    SQLModel.metadata.create_all(engine)
