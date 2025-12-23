from app.main import app
from fastapi.testclient import TestClient


async def test_healthz():
    client = TestClient(app)
    resp = client.get("/healthz")
    assert resp.json() == {"status": "ok"}
