import streamlit as st
import time
import random
from datetime import datetime, timedelta

# Page configuration
st.set_page_config(
    page_title="Threads Clone",
    page_icon="🧵",
    layout="centered",
    initial_sidebar_state="collapsed"
)

# Apply custom CSS
st.markdown("""
<style>
    .block-container {
        padding-top: 1rem;
        padding-bottom: 0rem;
        padding-left: 1rem;
        padding-right: 1rem;
    }
    .element-container {
        margin-bottom: 0.5rem;
    }
    div[data-testid="stVerticalBlock"] > div:has(div.stButton) {
        margin-bottom: 0rem;
    }
    .main .block-container {
        max-width: 38rem;
    }
    
    /* Custom thread post styling */
    .thread-post {
        padding: 12px 16px;
        border-bottom: 1px solid #eee;
        margin-bottom: 8px;
    }
    .username {
        font-weight: bold;
        margin-bottom: 2px;
    }
    .timestamp {
        color: #777;
        font-size: 0.8em;
        margin-bottom: 10px;
    }
    .post-content {
        margin-bottom: 10px;
    }
    .post-actions {
        display: flex;
        gap: 20px;
        color: #555;
    }
    /* Profile image styling */
    .profile-header {
        display: flex;
        align-items: center;
        margin-bottom: 6px;
    }
    .profile-img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-right: 10px;
    }
</style>
""", unsafe_allow_html=True)

# Initialize session state
if 'posts' not in st.session_state:
    st.session_state.posts = []

if 'load_count' not in st.session_state:
    st.session_state.load_count = 0

if 'total_posts' not in st.session_state:
    st.session_state.total_posts = 50  # Total posts in our virtual database

# Sample data generation
user_names = ["alex_design", "maya_travels", "tech_sam", "fitness_jamie", 
              "cooking_pat", "nature_taylor", "music_jordan", "art_casey"]
              
profile_pics = [
    "https://randomuser.me/api/portraits/men/32.jpg",
    "https://randomuser.me/api/portraits/women/44.jpg",
    "https://randomuser.me/api/portraits/men/22.jpg",
    "https://randomuser.me/api/portraits/women/56.jpg",
    "https://randomuser.me/api/portraits/men/41.jpg",
    "https://randomuser.me/api/portraits/women/68.jpg",
    "https://randomuser.me/api/portraits/men/17.jpg",
    "https://randomuser.me/api/portraits/women/33.jpg"
]

sample_posts = [
    "Just finished a new design project! What do you think?",
    "Exploring new coffee shops today. Any recommendations?",
    "The sunrise this morning was absolutely breathtaking.",
    "Working on a new coding project using Python and Streamlit!",
    "I can't believe how fast this week went by.",
    "What's everyone reading these days? Need book recommendations!",
    "Just tried that new restaurant downtown. Absolutely worth it!",
    "Does anyone else feel like algorithms control our lives now?",
    "Considering a digital detox this weekend. Any tips?",
    "Remember when we had to actually remember phone numbers?",
    "Just adopted a new puppy! Name suggestions welcome.",
    "Hot take: cereal is just breakfast soup.",
    "Starting a new fitness challenge tomorrow. Wish me luck!",
    "Can't stop thinking about that movie ending...",
    "Do you ever just stare at your code and wonder where you went wrong in life?",
    "Found the perfect hiking trail today. Views were incredible!",
    "Sometimes I think my cat is plotting against me.",
    "When did 'adulting' get so complicated?",
    "Just meal prepped for the entire week. Feeling accomplished!",
    "The weather app said no rain. The weather app LIED."
]

def generate_random_post(post_id):
    """Generate a random post with given ID"""
    user_index = random.randint(0, len(user_names) - 1)
    username = user_names[user_index]
    profile_pic = profile_pics[user_index]
    content = random.choice(sample_posts)
    
    # Create a random timestamp within the last week
    now = datetime.now()
    random_hours = random.randint(1, 168)  # within a week
    timestamp = now - timedelta(hours=random_hours)
    
    # Random engagement metrics
    likes = random.randint(5, 1500)
    comments = random.randint(0, 100)
    reposts = random.randint(0, 50)
    
    return {
        "id": post_id,
        "username": username,
        "profile_pic": profile_pic,
        "content": content,
        "timestamp": timestamp,
        "likes": likes,
        "comments": comments,
        "reposts": reposts
    }

def format_timestamp(timestamp):
    """Format timestamp to relative time (like '2h ago')"""
    now = datetime.now()
    diff = now - timestamp
    
    if diff.days > 0:
        return f"{diff.days}d ago"
    hours = diff.seconds // 3600
    if hours > 0:
        return f"{hours}h ago"
    minutes = (diff.seconds % 3600) // 60
    if minutes > 0:
        return f"{minutes}m ago"
    return "just now"

def load_more_posts():
    """Load more posts into the session state"""
    posts_per_load = 5
    start_idx = st.session_state.load_count * posts_per_load
    end_idx = start_idx + posts_per_load
    
    # Don't exceed total posts
    end_idx = min(end_idx, st.session_state.total_posts)
    
    # Generate and add new posts
    for i in range(start_idx, end_idx):
        post = generate_random_post(i)
        st.session_state.posts.append(post)
    
    st.session_state.load_count += 1

# App UI
with st.container():
    # App header
    col1, col2 = st.columns([1, 10])
    
    with col1:
        st.image("https://img.icons8.com/ios-filled/50/000000/thread.png", width=30)
    with col2:
        st.title("Threads")
    
    st.markdown("<hr style='margin: 0; padding: 0; margin-bottom: 10px;'>", unsafe_allow_html=True)

# Load initial posts if empty
if len(st.session_state.posts) == 0:
    load_more_posts()

# Display all posts
for post in st.session_state.posts:
    with st.container():
        st.markdown(f"""
        <div class="thread-post">
            <div class="profile-header">
                <img src="{post['profile_pic']}" class="profile-img">
                <div>
                    <div class="username">{post['username']}</div>
                    <div class="timestamp">{format_timestamp(post['timestamp'])}</div>
                </div>
            </div>
            <div class="post-content">{post['content']}</div>
            <div class="post-actions">
                <span>❤️ {post['likes']}</span>
                <span>💬 {post['comments']}</span>
                <span>🔄 {post['reposts']}</span>
            </div>
        </div>
        """, unsafe_allow_html=True)

# Infinite scroll implementation
# We'll use a placeholder at the bottom to trigger loading more content
scroll_placeholder = st.empty()

# Check if we've reached the end of available posts
if len(st.session_state.posts) < st.session_state.total_posts:
    with scroll_placeholder:
        # Show loading indicator when near the bottom
        with st.container():
            col1, col2, col3 = st.columns([1, 1, 1])
            with col2:
                if st.button("Load more", key="load_more"):
                    load_more_posts()
                    st.rerun()
                    
            # Auto-load more posts when scrolling down
            # This is a workaround since Streamlit doesn't have native scroll detection
            auto_load = st.empty()
            # Add slight delay before auto-loading more posts
            time.sleep(0.5)
            with auto_load:
                st.markdown("<div style='height: 100px;'></div>", unsafe_allow_html=True)
                load_more_posts()
                st.rerun()
else:
    st.markdown("<div style='text-align: center; color: #888; padding: 20px;'>You've reached the end!</div>", unsafe_allow_html=True)

# Add new post floating button
st.markdown("""
<div style="position: fixed; bottom: 20px; right: 20px; z-index: 1000;">
    <button style="background-color: black; color: white; border-radius: 50%; width: 60px; height: 60px; font-size: 24px; border: none; cursor: pointer;">+</button>
</div>
""", unsafe_allow_html=True)
