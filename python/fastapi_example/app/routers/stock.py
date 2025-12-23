import logging

from fastapi import APIRouter, Query
from pydantic import BaseModel

router = APIRouter()

db = {"AAPL": 3310.0, "TSLA": 200.4}


class StockPricingResponse(BaseModel):
    symbols: dict[str, float]


@router.get("/stock/pricing", response_model=StockPricingResponse)
async def get_stock_pricing(symbols: list[str] = Query(...)):
    logging.info(symbols)
    prices: dict[str, float] = {}
    for symbol in symbols:
        prices[symbol] = db.get(symbol)

    return StockPricingResponse(symbols=prices)
