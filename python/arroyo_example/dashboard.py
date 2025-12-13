#!/usr/bin/env python3
"""
Streamlit dashboard for BTC price monitoring.
Displays real-time prices, window aggregations, and alerts.
Pattern: Based on flink_example/dashboard_redis.py
"""

import streamlit as st
import pandas as pd
import redis
import json
from datetime import datetime
import time
import plotly.graph_objects as go
import os

# Redis configuration
REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", "16379"))

# Redis keys (matching redis_sink.py)
KEY_LATEST_PRICE = "btc:latest_price"
KEY_PRICE_HISTORY = "btc:price_history"
KEY_WINDOW_1M = "btc:window:1m"
KEY_WINDOW_5M = "btc:window:5m"
KEY_ALERTS = "btc:alerts"
KEY_ALERT_COUNT = "btc:alert_count"


@st.cache_resource
def get_redis_connection():
    """Get Redis connection."""
    return redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)


def fetch_latest_price(r):
    """Fetch latest BTC price."""
    try:
        data = r.get(KEY_LATEST_PRICE)
        return json.loads(data) if data else None
    except Exception as e:
        st.error(f"Error fetching price: {e}")
        return None


def fetch_price_history(r, limit=100):
    """Fetch price history from sorted set."""
    try:
        results = r.zrange(KEY_PRICE_HISTORY, -limit, -1, withscores=True)
        if not results:
            return pd.DataFrame()

        prices = []
        for data_json, score in results:
            data = json.loads(data_json)
            prices.append(
                {
                    "timestamp": datetime.fromtimestamp(score),
                    "price": data["price"],
                }
            )
        return pd.DataFrame(prices)
    except Exception as e:
        st.error(f"Error fetching history: {e}")
        return pd.DataFrame()


def fetch_window_stats(r, window_type="1m"):
    """Fetch window aggregation stats."""
    try:
        key = KEY_WINDOW_1M if window_type == "1m" else KEY_WINDOW_5M
        data = r.get(key)
        return json.loads(data) if data else None
    except Exception as e:
        st.error(f"Error fetching {window_type} window: {e}")
        return None


def fetch_alerts(r, limit=10):
    """Fetch recent alerts."""
    try:
        alerts = r.lrange(KEY_ALERTS, 0, limit - 1)
        return [json.loads(a) for a in alerts] if alerts else []
    except Exception as e:
        st.error(f"Error fetching alerts: {e}")
        return []


def fetch_alert_count(r):
    """Fetch total alert count."""
    try:
        count = r.get(KEY_ALERT_COUNT)
        return int(count) if count else 0
    except:
        return 0


def main():
    """Main Streamlit dashboard."""
    st.set_page_config(
        page_title="BTC Price Monitor - Faust Stream Processing",
        page_icon="₿",
        layout="wide",
    )

    st.title("₿ BTC Price Monitor")
    st.markdown("**Powered by:** Faust + AutoMQ + Redis")

    # Sidebar
    with st.sidebar:
        st.header("Settings")
        refresh_rate = st.slider("Auto-refresh (sec)", 1, 10, 2)

        st.markdown("---")
        st.markdown("### Architecture")
        st.code(
            """
yliveticker (Yahoo WS)
    ↓
AutoMQ (Kafka)
    ↓
Faust Agents
- Tumbling Windows
- Alert Detection
    ↓
Redis Cache
    ↓
Streamlit
        """,
            language="text",
        )

        st.markdown("---")
        st.markdown("### Window Sizes (Demo)")
        st.markdown("""
        - **1-min window**: 10 sec
        - **5-min window**: 30 sec
        - **Alert threshold**: 0.1%+
        """)

    # Main content
    try:
        r = get_redis_connection()
        r.ping()

        # Top row: Current price and change
        latest = fetch_latest_price(r)

        col1, col2, col3, col4 = st.columns(4)

        with col1:
            if latest:
                st.metric(
                    "BTC-USD",
                    f"${latest['price']:,.2f}",
                    f"{latest['change_pct']:+.2f}%",
                )
            else:
                st.metric("BTC-USD", "Loading...", "")

        # Window stats
        window_1m = fetch_window_stats(r, "1m")
        window_5m = fetch_window_stats(r, "5m")

        with col2:
            if window_1m:
                st.metric(
                    "1-Min Avg",
                    f"${window_1m['avg_price']:,.2f}",
                    f"{window_1m['price_change_pct']:+.2f}%",
                )
            else:
                st.metric("1-Min Avg", "Waiting...", "")

        with col3:
            if window_5m:
                st.metric(
                    "5-Min Avg",
                    f"${window_5m['avg_price']:,.2f}",
                    f"{window_5m['price_change_pct']:+.2f}%",
                )
            else:
                st.metric("5-Min Avg", "Waiting...", "")

        with col4:
            alert_count = fetch_alert_count(r)
            st.metric("Total Alerts", alert_count)

        st.markdown("---")

        # Price chart and alerts side by side
        chart_col, alert_col = st.columns([2, 1])

        with chart_col:
            st.subheader("Price Chart (Real-time)")

            history_df = fetch_price_history(r, limit=100)

            if not history_df.empty:
                fig = go.Figure()
                fig.add_trace(
                    go.Scatter(
                        x=history_df["timestamp"],
                        y=history_df["price"],
                        mode="lines+markers",
                        name="BTC-USD",
                        line=dict(color="#F7931A", width=2),
                        marker=dict(size=4),
                    )
                )

                # Add horizontal lines for window averages
                if window_1m:
                    fig.add_hline(
                        y=window_1m["avg_price"],
                        line_dash="dash",
                        line_color="blue",
                        annotation_text="1m avg",
                    )
                if window_5m:
                    fig.add_hline(
                        y=window_5m["avg_price"],
                        line_dash="dot",
                        line_color="green",
                        annotation_text="5m avg",
                    )

                fig.update_layout(
                    height=400,
                    margin=dict(l=0, r=0, t=30, b=0),
                    yaxis_title="Price (USD)",
                    xaxis_title="Time",
                )
                st.plotly_chart(fig, use_container_width=True)
            else:
                st.info("Waiting for price data...")

        with alert_col:
            st.subheader("Recent Alerts")

            alerts = fetch_alerts(r, limit=10)

            if alerts:
                for alert in alerts:
                    severity = alert["severity"]
                    if severity == "HIGH":
                        color = "red"
                        icon = "🔴"
                    elif severity == "MEDIUM":
                        color = "orange"
                        icon = "🟠"
                    else:
                        color = "yellow"
                        icon = "🟡"

                    with st.container():
                        st.markdown(f"""
                        {icon} **{alert["alert_type"]}** ({severity})

                        {alert["change_pct"]:+.2f}% | ${alert["reference_price"]:.2f} → ${alert["current_price"]:.2f}

                        *{alert["triggered_at"][:19]}*

                        ---
                        """)
            else:
                st.info("No alerts yet. Waiting for price movements...")

        # Window details
        st.markdown("---")
        st.subheader("Window Aggregation Details")

        wcol1, wcol2 = st.columns(2)

        with wcol1:
            st.markdown("#### 1-Minute Window (Demo: 10 sec)")
            if window_1m:
                st.json(
                    {
                        "window_start": window_1m["window_start"],
                        "window_end": window_1m["window_end"],
                        "open": f"${window_1m['open_price']:.2f}",
                        "close": f"${window_1m['close_price']:.2f}",
                        "high": f"${window_1m['high_price']:.2f}",
                        "low": f"${window_1m['low_price']:.2f}",
                        "avg": f"${window_1m['avg_price']:.2f}",
                        "ticks": window_1m["tick_count"],
                    }
                )
            else:
                st.warning("Waiting for first window...")

        with wcol2:
            st.markdown("#### 5-Minute Window (Demo: 30 sec)")
            if window_5m:
                st.json(
                    {
                        "window_start": window_5m["window_start"],
                        "window_end": window_5m["window_end"],
                        "open": f"${window_5m['open_price']:.2f}",
                        "close": f"${window_5m['close_price']:.2f}",
                        "high": f"${window_5m['high_price']:.2f}",
                        "low": f"${window_5m['low_price']:.2f}",
                        "avg": f"${window_5m['avg_price']:.2f}",
                        "ticks": window_5m["tick_count"],
                    }
                )
            else:
                st.warning("Waiting for first window...")

        # Footer
        st.caption(f"Last updated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")

    except redis.ConnectionError:
        st.error("Cannot connect to Redis")
        st.info("Start Redis: `docker-compose up -d redis`")
    except Exception as e:
        st.error(f"Error: {e}")

    # Auto-refresh
    time.sleep(refresh_rate)
    st.rerun()


if __name__ == "__main__":
    main()
