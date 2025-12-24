import http.client

from app.main import app
from fastapi.testclient import TestClient

client = TestClient(app)


def test_get_stock_pricing():
    symbol = "AAPL"
    resp = client.get(f"/stock/pricing/{symbol}")
    assert http.client.OK == resp.status_code
    assert {"symbol": {"name": "AAPL", "price": 3310.0}} == resp.json()


def test_get_stock_pricing_not_found():
    symbol = "NVDA"
    resp = client.get(f"/stock/pricing/{symbol}")
    assert http.client.NOT_FOUND == resp.status_code
