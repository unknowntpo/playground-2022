from app.main import app
from fastapi.testclient import TestClient

client = TestClient(app)


def test_get_stock_pricing():
    resp = client.get("/stock/pricing", params={"symbols": ["AAPL"]})
    assert resp.json() == {"symbols": {"AAPL": 3310.0}}
