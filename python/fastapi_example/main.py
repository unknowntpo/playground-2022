
from typing import Union

from fastapi import FastAPI

app = FastAPI()

@app.get("/")
async def read_root() -> dict:
    return {"Hello": "World"}

@app.get("/items/{item_id}")
async def read_item(item_id: int, q: Union[str, None] = None) -> dict:
    return {"item_id": item_id, "q": q}
