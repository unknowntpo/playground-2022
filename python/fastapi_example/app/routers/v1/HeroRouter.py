from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy import select
from sqlmodel import Session

from app.infra.engine import engine, create_db_and_tables
from app.entities.hero import Hero

HeroRouter = APIRouter(prefix="/v1/heroes", tags=["item"])


def get_session():
    with Session(engine) as session:
        print(f" in get session: engine: {engine}")
        yield session


SessionDep = Annotated[Session, Depends(get_session)]


@HeroRouter.post("/heroes/", response_model=Hero)
def create_hero(hero: Hero, session: SessionDep) -> Hero:
    session.add(hero)
    session.commit()
    session.refresh(hero)
    return hero


@HeroRouter.get("/heroes/")
async def read_heroes(session: SessionDep) -> list[Hero]:
    rows = session.exec(select(Hero)).all()
    # row has type sqlalchemy.engine.row.Row, we need to get the Hero object from row._mapping
    return [row._mapping["Hero"] for row in rows]


@HeroRouter.get("/heroes/{hero_id}", response_model=Hero)
def read_hero(hero_id: int, session: SessionDep) -> Hero:
    hero = session.get(Hero, hero_id)
    if not hero:
        raise HTTPException(status_code=404, detail="Hero not found")
    return hero


@HeroRouter.delete("/heroes/{hero_id}", response_model=dict)
def delete_hero(hero_id: int, session: SessionDep):
    hero = session.get(Hero, hero_id)
    if not hero:
        raise HTTPException(status_code=404, detail="Hero not found")
    session.delete(hero)
    session.commit()
    return {"ok": True}
