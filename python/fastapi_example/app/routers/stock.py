import logging

from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()


class StockPricingResponse(BaseModel):
    symbols: dict[str, float]


@router.get("/stock/pricing", response_model=StockPricingResponse)
def get_stock_pricing(symbols: list[str] = None):
    logging.info(symbols)
    return StockPricingResponse(symbols={"AAPL": 3310.0})
