#!/usr/bin/env python3
"""
Streamlit dashboard for displaying trending blog posts from Redis.
Shows top 10 trending posts computed by Flink CDC pipeline.
"""
import streamlit as st
import pandas as pd
import redis
import json
from datetime import datetime
import time

# Redis configuration
REDIS_HOST = 'localhost'
REDIS_PORT = 16379
REDIS_KEY_TOP_POSTS = 'trending:top10'
REDIS_KEY_WINDOW_STATS = 'trending:window_stats'

@st.cache_resource
def get_redis_connection():
    """Get Redis connection."""
    return redis.Redis(
        host=REDIS_HOST,
        port=REDIS_PORT,
        decode_responses=True
    )

def fetch_top_posts_from_redis(redis_client, limit=10):
    """Fetch top trending posts from Redis."""
    try:
        # Get top posts from sorted set (already sorted by view count)
        results = redis_client.zrange(REDIS_KEY_TOP_POSTS, 0, limit - 1)

        if not results:
            return pd.DataFrame()

        # Parse JSON results
        posts = []
        for i, result_json in enumerate(results):
            result = json.loads(result_json)
            posts.append({
                'rank': i + 1,
                'post_id': result['post_id'],
                'title': result['title'],
                'view_count': result['view_count'],
                'window_start': result.get('window_start', ''),
                'window_end': result.get('window_end', '')
            })

        return pd.DataFrame(posts)

    except Exception as e:
        st.error(f"Error fetching from Redis: {e}")
        return pd.DataFrame()

def fetch_window_stats(redis_client):
    """Fetch window statistics from Redis."""
    try:
        stats_json = redis_client.get(REDIS_KEY_WINDOW_STATS)
        if stats_json:
            return json.loads(stats_json)
        return None
    except Exception as e:
        st.error(f"Error fetching window stats: {e}")
        return None

def fetch_total_posts_count(redis_client):
    """Get total number of posts being tracked."""
    try:
        return redis_client.zcard(REDIS_KEY_TOP_POSTS)
    except:
        return 0

def main():
    """Main Streamlit app."""
    st.set_page_config(
        page_title="Trending Blog Posts (Flink CDC + Redis)",
        page_icon="🚀",
        layout="wide"
    )

    st.title("🚀 Real-time Trending Blog Posts")
    st.markdown("**Powered by:** Flink CDC + PostgreSQL CDC + Redis")

    # Auto-refresh
    placeholder = st.empty()

    # Sidebar
    with st.sidebar:
        st.header("⚙️ Settings")
        refresh_rate = st.slider("Auto-refresh (seconds)", 1, 30, 3)
        top_n = st.slider("Top N posts", 5, 20, 10)

        st.markdown("---")
        st.markdown("### 📊 Architecture")
        st.markdown("""
        ```
        Load Generator
            ↓
        PostgreSQL (CDC enabled)
            ↓
        Flink CDC Connector
            ↓
        PyFlink Processing
        - 1-min tumbling windows
        - Top-K aggregation
            ↓
        Redis (Sorted Set)
            ↓
        Streamlit Dashboard
        ```
        """)

        st.markdown("---")
        st.markdown("### 🔧 Components")
        st.markdown("""
        - **CDC**: PostgreSQL logical replication
        - **Stream Processing**: Apache Flink
        - **Cache**: Redis (sorted sets)
        - **Visualization**: Streamlit
        """)

    # Main content area
    try:
        redis_client = get_redis_connection()

        # Test Redis connection
        redis_client.ping()

        # Fetch window stats
        window_stats = fetch_window_stats(redis_client)

        # Display window information
        if window_stats:
            col1, col2, col3 = st.columns(3)
            with col1:
                st.metric("Window Start", window_stats['window_start'].split('.')[0] if window_stats.get('window_start') else 'N/A')
            with col2:
                st.metric("Window End", window_stats['window_end'].split('.')[0] if window_stats.get('window_end') else 'N/A')
            with col3:
                updated = window_stats.get('updated_at', 'N/A')
                if updated != 'N/A':
                    try:
                        updated_dt = datetime.fromisoformat(updated)
                        updated = updated_dt.strftime('%H:%M:%S')
                    except:
                        pass
                st.metric("Last Updated", updated)
        else:
            st.info("⏳ Waiting for Flink pipeline to process first window...")

        st.markdown("---")

        # Fetch and display top posts
        df = fetch_top_posts_from_redis(redis_client, limit=top_n)

        if not df.empty:
            st.subheader(f"🔥 Top {len(df)} Trending Posts (Real-time)")

            # Display metrics
            total_views = df['view_count'].sum()
            avg_views = df['view_count'].mean()

            metric_col1, metric_col2, metric_col3 = st.columns(3)
            with metric_col1:
                st.metric("Total Views (Top Posts)", f"{int(total_views):,}")
            with metric_col2:
                st.metric("Average Views", f"{int(avg_views):,}")
            with metric_col3:
                st.metric("Posts Tracked", len(df))

            st.markdown("---")

            # Display as table with better formatting
            display_df = df.copy()
            display_df = display_df[['rank', 'title', 'view_count']]
            display_df.columns = ['🏆 Rank', '📝 Post Title', '👁️ Views']

            # Apply styling
            st.dataframe(
                display_df,
                use_container_width=True,
                hide_index=True,
                height=400
            )

            # Display as bar chart
            st.subheader("📈 View Count Distribution")
            chart_df = df[['title', 'view_count']].copy()
            chart_df['title'] = chart_df['title'].str[:40] + '...'
            chart_df = chart_df.set_index('title')
            st.bar_chart(chart_df, height=300)

            # Show detailed stats for top post
            if len(df) > 0:
                st.markdown("---")
                st.subheader("🥇 Top Post Details")
                top_post = df.iloc[0]
                col1, col2, col3, col4 = st.columns(4)
                with col1:
                    st.metric("Rank", f"#{top_post['rank']}")
                with col2:
                    st.metric("Post ID", top_post['post_id'])
                with col3:
                    st.metric("Views", f"{top_post['view_count']:,}")
                with col4:
                    view_pct = (top_post['view_count'] / total_views * 100) if total_views > 0 else 0
                    st.metric("Share", f"{view_pct:.1f}%")

                st.info(f"**Title:** {top_post['title']}")

        else:
            st.warning("📭 No trending data available yet.")
            st.markdown("""
            **To see data:**
            1. Start the load generator: `./run.sh load-generator 10`
            2. Start the Flink CDC pipeline: `uv run python flink_cdc_pipeline.py`
            3. Wait for the first 1-minute window to complete
            """)

        # Last updated
        st.caption(f"Dashboard updated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")

        # Redis status
        with st.expander("🔍 Redis Status"):
            st.write(f"**Host:** {REDIS_HOST}:{REDIS_PORT}")
            st.write(f"**Connection:** ✅ Connected")
            st.write(f"**Key:** `{REDIS_KEY_TOP_POSTS}`")

            # Show raw Redis data
            if st.button("Show Raw Redis Data"):
                raw_data = redis_client.zrange(REDIS_KEY_TOP_POSTS, 0, -1, withscores=True)
                st.json([{
                    'data': json.loads(item[0]),
                    'score': item[1]
                } for item in raw_data])

    except redis.ConnectionError:
        st.error("❌ Cannot connect to Redis")
        st.info("""
        **Make sure Redis is running:**
        ```bash
        docker-compose up -d redis
        ```
        """)
    except Exception as e:
        st.error(f"❌ Error: {e}")
        st.info("Check that all services are running properly.")

    # Auto-refresh
    time.sleep(refresh_rate)
    st.rerun()

if __name__ == '__main__':
    main()
