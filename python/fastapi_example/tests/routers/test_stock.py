import http.client

from app.main import app
from fastapi.testclient import TestClient

client = TestClient(app)


def test_get_stock_pricing():
    resp = client.get("/stock/pricing", params={"symbols": ["TSLA", "AAPL"]})
    assert http.client.OK == resp.status_code
    assert {"symbols": {"TSLA": 200.4, "AAPL": 3310.0}} == resp.json()
