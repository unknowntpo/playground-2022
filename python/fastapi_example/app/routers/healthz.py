from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()


class HealthzResponse(BaseModel):
    status: str


@router.get("/healthz", response_model=HealthzResponse)
def healthz():
    return HealthzResponse(status="ok")
