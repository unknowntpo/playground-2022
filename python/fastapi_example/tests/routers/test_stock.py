import http.client

from app.main import app
from fastapi.testclient import TestClient

client = TestClient(app)


def test_get_stock_pricing():
    resp = client.get("/stock/pricing", params={"symbols": ["AAPL"]})
    assert http.client.OK == resp.status_code
    assert {"symbols": {"AAPL": 3310.0}} == resp.json()
    resp = client.get("/stock/pricing", params={"symbols": ["TSLA"]})
    assert http.client.OK == resp.status_code
    assert {"symbols": {"TSLA": 200.4}} == resp.json()
