from typing import Union
from fastapi import FastAPI
from pydantic import BaseModel, EmailStr, constr

app = FastAPI()

class Item(BaseModel):
    name: str
    price: float
    is_offer: Union[bool, None] = None

class User(BaseModel):
    name: constr(max_length=15)
    email: EmailStr

@app.get("/")
async def read_root() -> dict:
    return {"Hello": "World"}

@app.get("/healthz")
async def healthcheck() -> dict:
    return {"status": "ok"}

@app.get("/items/{item_id}")
async def read_item(item_id: int, q: Union[str, None] = None) -> dict:
    return {"item_id": item_id, "q": q}

@app.put("/items/{item_id}")
def update_item(item_id: int, item: Item):
    return {"item_name": item.name, "item_id": item_id}

@app.put("/users")
def add_user(user: User):
    return {"user": user}
