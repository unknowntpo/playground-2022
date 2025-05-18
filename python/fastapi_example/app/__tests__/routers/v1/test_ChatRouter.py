import http
import json

import pytest
from fastapi.testclient import TestClient
from sqlmodel import Session, select

from app.__tests__.utils.utils import trunk_all_tables
from app.entities.chat_message import ChatMessage
from app.entities.item import Item
from app.infra.engine import engine
from app.main import app
from app.routers.v1.ChatRouter import ChatMessageCreateRequest
from app.routers.v1.HeroRouter import get_session


# Create a fixture that will be used for all tests
# Use `with` to use TestClient as a context manager
@pytest.fixture
def client():
    with TestClient(app) as client:
        yield client


@pytest.fixture(autouse=True)
def before_each():
    trunk_all_tables()


def test_ChatRouter_chat(client):
    with client.websocket_connect("/v1/chat/ws") as ws:
        msg = ChatMessageCreateRequest(content="Hello from user1")
        ws.send_json(msg.model_dump())
        got = ws.receive_json()
        with Session(engine) as session:
            rows = session.exec(select(ChatMessage)).all()
            assert len(rows) == 1
            # FIXME: why this is ChatMessage, not sqlalchemy.engine.row.Row ?
            ChatMessage
            got_msg = rows[0]
            assert got_msg.content == msg.content
        # When userA connected, it get all history messages, and when it send data,
        # userB will get all messages.
