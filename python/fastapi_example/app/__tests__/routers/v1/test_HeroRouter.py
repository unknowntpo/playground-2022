import http
import json

import pytest
from fastapi.testclient import TestClient

from app.entities.hero import Hero
from app.entities.item import Item
from app.main import app

# Create a fixture that will be used for all tests
# Use `with` to use TestClient as a context manager
@pytest.fixture
def client():
    with TestClient(app) as client:
        yield client

def test_HeroRouter_get(client):
    response = client.get(f"/v1/heroes/heroes/")
    assert response.status_code == 200
    assert response.json() == []
