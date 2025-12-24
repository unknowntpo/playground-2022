import http.client
import logging

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel

router = APIRouter()


class SymbolData(BaseModel):
    name: str
    price: float


class StockPricingResponse(BaseModel):
    symbol: SymbolData


class SymbolNotFoundException(Exception):
    pass


@router.get("/stock/pricing/{symbol}", response_model=StockPricingResponse)
async def get_stock_pricing(symbol: str):
    logging.info(f"got symbol: {symbol}")
    try:
        price = await get_price(symbol)
        return StockPricingResponse(symbol=SymbolData(name=symbol, price=price))
    except SymbolNotFoundException:
        raise HTTPException(
            # FIXME: lower case or uppercase ?
            status_code=http.client.NOT_FOUND,
            detail=f"symbol {symbol} not found",
        )


db = {"AAPL": 3310.0, "TSLA": 200.4}


async def get_price(symbol: str) -> float:
    price = db.get(symbol)
    if not price:
        raise SymbolNotFoundException(f"symbol {symbol} not found")
    return price
