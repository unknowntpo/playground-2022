from datetime import datetime, UTC

from sqlmodel import SQLModel, Field, String, Float


class Stock(SQLModel, table=True):
    __tablename__ = "stock"
    id: int | None = Field(default=None, primary_key=True, index=True)
    symbol: str = Field(unique=True, index=True)
    price: float = Field(nullable=False)
    created_at: datetime = Field(default_factory=lambda: datetime.now(UTC), nullable=False)
    updated_at: datetime = Field(default_factory=lambda: datetime.now(UTC), nullable=False)

    def __repr__(self) -> str:
        return f"Stock(id={self.id!r}, symbol={self.symbol!r}, price={self.price!r}, updated_at={self.updated_at!r})"
