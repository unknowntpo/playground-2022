from contextlib import asynccontextmanager
from typing import Union, Annotated
from fastapi import FastAPI, Depends, Query, HTTPException
from pydantic import BaseModel, EmailStr, constr
from sqlalchemy import select
from sqlmodel import Session, SQLModel

from engine import create_db_and_tables, engine
from entities.hero import Hero
from entities.item import Item
from routers.v1.HeroRouter import HeroRouter
from routers.v1.ItemRouter import ItemRouter
from routers.v1.healthz import HealthzRouter


@asynccontextmanager
async def lifespan(app):
    print("xxx lifespan startup")
    create_db_and_tables()
    yield
    # Optional: cleanup code here

app = FastAPI(lifespan=lifespan)

app.include_router(HealthzRouter)
app.include_router(ItemRouter)
app.include_router(HeroRouter)

class User(BaseModel):
    name: constr(max_length=15)
    email: EmailStr


@app.get("/")
async def read_root() -> dict:
    return {"Hello": "World"}


@app.put("/users")
def add_user(user: User):
    return {"user": user}
