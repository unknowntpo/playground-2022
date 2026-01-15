from enum import StrEnum

from pydantic import BaseModel


class EventType(StrEnum):
    Kill = "kill"
    Death = "death"

class Event(BaseModel):
    player_id: str
    type: EventType
    # FIXME: unix timestamp or datetime object ?
    timestamp: float
