import pytest
from fastapi.testclient import TestClient
from main import app

# Create a fixture that will be used for all tests
@pytest.fixture
def client():
    return TestClient(app)


def test_healthz(client):
    response = client.get("/v1/healthz")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}
