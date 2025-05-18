import http
import json

import pytest
from fastapi.testclient import TestClient

from app.entities.item import Item
from app.main import app

# Create a fixture that will be used for all tests
@pytest.fixture
def client():
    return TestClient(app)

def test_ChatRouter_chat(client):
    with client.websocket_connect("/v1/chat/ws") as ws:
        data = "hello"
        ws.send_text(data)
        got = ws.receive_text()
        assert got == f"Message text was: {data}"
