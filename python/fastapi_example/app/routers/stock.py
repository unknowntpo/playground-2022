import http.client
import logging
from typing import Annotated
from sqlmodel import Session, select

from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel

from app.infras.database import get_session
from app.repos.stock_repo import Stock

router = APIRouter()

SessionDep = Annotated[Session, Depends(get_session)]


class SymbolData(BaseModel):
    name: str
    price: float


class StockPricingResponse(BaseModel):
    symbol: SymbolData


class SymbolNotFoundException(Exception):
    pass


@router.get("/stock/pricing/{symbol}", response_model=StockPricingResponse)
async def get_stock_pricing(symbol: str, session: SessionDep):
    logging.info(f"got symbol: {symbol}")
    try:
        price = await get_price(symbol, session)
        return StockPricingResponse(symbol=SymbolData(name=symbol, price=price))
    except SymbolNotFoundException:
        raise HTTPException(
            # FIXME: lower case or uppercase ?
            status_code=http.client.NOT_FOUND,
            detail=f"symbol {symbol} not found",
        )


async def get_price(symbol: str, session: Session) -> float:
    stock = session.exec(select(Stock).where(Stock.symbol == symbol)).first()
    if not stock:
        raise SymbolNotFoundException(f"symbol {symbol} not found")
    return stock.price
