import http
import json

import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlmodel import SQLModel, Session

from app.entities.hero import Hero
from app.entities.item import Item
from app.infra.engine import engine
from app.main import app
from app.routers.v1.HeroRouter import get_session
from app.__tests__.utils.utils import trunk_all_tables

# Create a fixture that will be used for all tests
# Use `with` to use TestClient as a context manager
@pytest.fixture
def client():
    with TestClient(app) as client:
        yield client

@pytest.fixture(autouse=True)
def before_each():
    trunk_all_tables()

def test_HeroRouter_get(client):
    hero0 = Hero(name="Car", secret_name="Carl", age=40)
    response = client.post("/v1/heroes/heroes/", json=hero0.model_dump())
    assert response.status_code == http.HTTPStatus.CREATED

    hero1 = Hero(name="Batman", secret_name="Robin", age=35)
    response = client.post("/v1/heroes/heroes/", json=hero1.model_dump())
    assert response.status_code == http.HTTPStatus.CREATED

    response = client.get(f"/v1/heroes/heroes/")
    assert response.status_code == 200
    # FIXME:
    # should exclude id field
    # route should not have two heroes
    # pytest, pycharm, should have same test result (TEST=True should be set)
    heroes = response.json()
    assert len(heroes) == 2
    assert heroes[0]["name"] == hero0.name
    assert heroes[1]["name"] == hero1.name
