"""Record models for BTC price monitoring."""

from dataclasses import dataclass
from typing import Optional


@dataclass
class PriceTick:
    """Raw price tick from yliveticker."""

    symbol: str  # e.g., "BTC-USD"
    price: float  # Current price
    timestamp: float  # Unix timestamp
    change: float  # Price change
    change_percent: float  # Percent change
    day_high: Optional[float] = None
    day_low: Optional[float] = None
    volume: Optional[int] = None


@dataclass
class WindowAggregation:
    """Aggregated window statistics."""

    symbol: str
    window_start: str  # ISO format
    window_end: str  # ISO format
    window_size_sec: int  # 60 or 300
    open_price: float  # First price in window
    close_price: float  # Last price in window
    high_price: float  # Max in window
    low_price: float  # Min in window
    avg_price: float  # Average
    price_change: float  # close - open
    price_change_pct: float  # Percentage change
    tick_count: int  # Number of ticks


@dataclass
class Alert:
    """Price alert event."""

    symbol: str
    alert_type: str  # "PRICE_SPIKE" | "PRICE_DROP" | "VOLATILITY"
    severity: str  # "LOW" | "MEDIUM" | "HIGH"
    message: str
    current_price: float
    reference_price: float  # Price we're comparing against
    change_pct: float
    triggered_at: str  # ISO format
    window_size_sec: int
