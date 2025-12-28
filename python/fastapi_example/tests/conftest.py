import pytest
from sqlmodel import SQLModel, Session, text

from app.infras.database import engine


@pytest.fixture(scope="session", autouse=True)
def setup_db():
    """
    BEFORE ALL (Session Scope)
    Runs once per test session.
    """
    SQLModel.metadata.drop_all(engine)
    SQLModel.metadata.create_all(engine)
    yield


@pytest.fixture(name="db_session")
def session_fixture():
    with Session(engine) as session:
        for table in SQLModel.metadata.tables:
            session.exec(text(f"DELETE FROM {table}"))
        session.commit()
        yield session
