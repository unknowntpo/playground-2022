import http.client

import pytest
from fastapi.testclient import TestClient

from app.main import app
from app.repos.stock_repo import Stock


@pytest.fixture
def client():
    with TestClient(app) as c:
        yield c


def test_get_stock_pricing(client, db_session):
    symbol = "AAPL"
    price = 3310.0
    db_session.add(Stock(symbol=symbol, price=3310.0))
    db_session.commit()

    resp = client.get(f"/stock/pricing/{symbol}")
    assert http.client.OK == resp.status_code
    assert {"symbol": {"name": symbol, "price": price}} == resp.json()


def test_get_stock_pricing_not_found(client):
    symbol = "NVDA"
    resp = client.get(f"/stock/pricing/{symbol}")
    assert http.client.NOT_FOUND == resp.status_code
