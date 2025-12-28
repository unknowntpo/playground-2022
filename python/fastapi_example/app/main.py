import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.infras.database import create_db_and_tables
from app.routers import healthz, stock


@asynccontextmanager
async def lifespan(app: FastAPI):
    create_db_and_tables()
    yield


app = FastAPI(lifespan=lifespan)


app.include_router(healthz.router)
app.include_router(stock.router)
