import http
import json

import pytest
from fastapi.testclient import TestClient

from entities.hero import Hero
from entities.item import Item
from main import app

# Create a fixture that will be used for all tests
@pytest.fixture
def client():
    return TestClient(app)

def test_HeroRouter_get(client):
    response = client.get(f"/v1/heroes/heroes/")
    assert response.status_code == 200
    assert response.json() == []

# def test_HeroRouter_post(client):
#     item = Hero(name="Car", price=1000.00)
#     expect_id = 3
#
#     response = client.post(f"/v1/items", json=item.model_dump())
#
#     assert response.status_code == http.HTTPStatus.CREATED
#     assert response.json() == {"item_name": item.name, "item_id": expect_id}
