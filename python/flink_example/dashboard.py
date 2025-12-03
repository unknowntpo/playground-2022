#!/usr/bin/env python3
"""
Streamlit dashboard for displaying trending blog posts.
Shows top 10 trending posts based on view counts in real-time.
"""
import streamlit as st
import pandas as pd
import psycopg2
from datetime import datetime
import time

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'port': 15432,
    'database': 'blogdb',
    'user': 'flinkuser',
    'password': 'flinkpass'
}

@st.cache_resource
def get_db_connection():
    """Get database connection."""
    return psycopg2.connect(**DB_CONFIG)

def fetch_top_posts(conn, limit=10):
    """Fetch top trending posts by view count."""
    query = """
        SELECT
            p.id,
            p.title,
            COUNT(ve.id) as view_count,
            MAX(ve.viewed_at) as last_viewed
        FROM posts p
        LEFT JOIN view_events ve ON p.id = ve.post_id
        WHERE ve.viewed_at >= NOW() - INTERVAL '1 hour'
        GROUP BY p.id, p.title
        ORDER BY view_count DESC
        LIMIT %s
    """
    df = pd.read_sql(query, conn, params=(limit,))
    return df

def fetch_stats(conn):
    """Fetch overall statistics."""
    query = """
        SELECT
            COUNT(DISTINCT post_id) as unique_posts_viewed,
            COUNT(*) as total_views,
            COUNT(DISTINCT user_id) as unique_users
        FROM view_events
        WHERE viewed_at >= NOW() - INTERVAL '1 hour'
    """
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    cursor.close()
    return {
        'unique_posts': row[0] if row else 0,
        'total_views': row[1] if row else 0,
        'unique_users': row[2] if row else 0
    }

def main():
    """Main Streamlit app."""
    st.set_page_config(
        page_title="Trending Blog Posts",
        page_icon="📊",
        layout="wide"
    )

    st.title("📊 Trending Blog Posts Dashboard")
    st.markdown("Real-time view of the most popular blog posts (last hour)")

    # Auto-refresh
    placeholder = st.empty()

    # Sidebar
    with st.sidebar:
        st.header("Settings")
        refresh_rate = st.slider("Auto-refresh (seconds)", 1, 30, 5)
        top_n = st.slider("Top N posts", 5, 20, 10)
        st.markdown("---")
        st.markdown("### About")
        st.markdown("""
        This dashboard shows real-time trending blog posts using:
        - **PostgreSQL** for data storage
        - **Flink CDC** for change data capture
        - **PyFlink** for stream processing
        - **Streamlit** for visualization
        """)

    # Main content area
    try:
        conn = get_db_connection()

        # Fetch stats
        stats = fetch_stats(conn)

        # Display metrics
        col1, col2, col3 = st.columns(3)
        with col1:
            st.metric("Total Views (1h)", f"{stats['total_views']:,}")
        with col2:
            st.metric("Unique Posts", stats['unique_posts'])
        with col3:
            st.metric("Unique Users", f"{stats['unique_users']:,}")

        st.markdown("---")

        # Fetch and display top posts
        df = fetch_top_posts(conn, limit=top_n)

        if not df.empty:
            st.subheader(f"🔥 Top {top_n} Trending Posts")

            # Display as table
            display_df = df.copy()
            display_df['rank'] = range(1, len(display_df) + 1)
            display_df = display_df[['rank', 'title', 'view_count', 'last_viewed']]
            display_df.columns = ['Rank', 'Post Title', 'Views', 'Last Viewed']

            st.dataframe(
                display_df,
                use_container_width=True,
                hide_index=True
            )

            # Display as bar chart
            st.subheader("📈 View Count Distribution")
            chart_df = df[['title', 'view_count']].head(10)
            chart_df['title'] = chart_df['title'].str[:50] + '...'
            st.bar_chart(chart_df.set_index('title'))

        else:
            st.info("No view events in the last hour. Start the load generator to see data.")

        # Last updated
        st.caption(f"Last updated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")

    except Exception as e:
        st.error(f"Error connecting to database: {e}")
        st.info("Make sure PostgreSQL is running and accessible.")

    # Auto-refresh
    time.sleep(refresh_rate)
    st.rerun()

if __name__ == '__main__':
    main()
