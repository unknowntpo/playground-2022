from fastapi import FastAPI

from app.routers import healthz

app = FastAPI()
app.include_router(healthz.router)
