from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy import select
from sqlmodel import Session

from engine import engine, create_db_and_tables
from entities import hero
from entities.hero import Hero

HeroRouter = APIRouter(prefix="/v1/heroes", tags=["item"])


def get_session():
    with Session(engine) as session:
        yield session


SessionDep = Annotated[Session, Depends(get_session)]


@HeroRouter.post("/heroes/", response_model=Hero)
def create_hero(hero: Hero, session: SessionDep) -> Hero:
    session.add(hero)
    session.commit()
    session.refresh(hero)
    return hero


@HeroRouter.get("/heroes/", response_model=list[Hero])
async def read_heroes(session: SessionDep) -> list[Hero]:
    heroes = session.exec(select(Hero)).all()
    print(heroes)
    return [Hero(id=3, name="Batman", age=10, secret_name="badman")]
    # return heroes


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
