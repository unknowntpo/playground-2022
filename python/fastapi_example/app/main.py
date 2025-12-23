from fastapi import FastAPI

from app.routers import healthz, stock

app = FastAPI()
app.include_router(healthz.router)
app.include_router(stock.router)
