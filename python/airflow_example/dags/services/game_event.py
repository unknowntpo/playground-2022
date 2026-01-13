from dataclasses import dataclass
from enum import Enum

from pydantic import BaseModel


class EventType(Enum):
    Kill = "kill"
    Death = "death"

class Event(BaseModel):
    player_id: str
    type: EventType
    # FIXME: unix timestamp or datetime object ?
    timestamp: float
