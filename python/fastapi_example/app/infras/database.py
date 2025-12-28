from sqlmodel import SQLModel, create_engine, Session
from app.config import settings

sqlite_url = f"sqlite:///{settings.sqlite_db_name}"

connect_args = {"check_same_thread": False}
engine = create_engine(sqlite_url, connect_args=connect_args)


def get_session():
    with Session(engine) as session:
        yield session


def create_db_and_tables():
    SQLModel.metadata.create_all(engine)
