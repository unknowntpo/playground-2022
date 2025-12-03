#!/usr/bin/env python3
"""
Load generator for simulating blog post view events.
Generates random view events and inserts them into PostgreSQL in real-time streaming fashion.
Simulates realistic user behavior patterns.
"""
import random
import time
import signal
import sys
from datetime import datetime
from typing import Optional
import psycopg2
from psycopg2.extensions import connection
from faker import Faker

fake = Faker()

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'port': 15432,
    'database': 'blogdb',
    'user': 'flinkuser',
    'password': 'flinkpass'
}

# Event generation configuration
DEFAULT_EVENTS_PER_SEC = 10

# Simulate realistic patterns
# Some posts are naturally more popular (power law distribution)
POPULARITY_WEIGHTS = None  # Will be initialized based on posts

class LoadGenerator:
    def __init__(self, events_per_sec: int = DEFAULT_EVENTS_PER_SEC):
        self.events_per_sec = events_per_sec
        self.running = True
        self.conn: Optional[connection] = None
        self.post_ids = []
        self.popularity_weights = []
        self.total_events = 0
        self.start_time = None

        # Setup signal handlers for graceful shutdown
        signal.signal(signal.SIGINT, self._signal_handler)
        signal.signal(signal.SIGTERM, self._signal_handler)

    def _signal_handler(self, signum, frame):
        """Handle shutdown signals gracefully."""
        print(f"\n[{datetime.now()}] Received signal {signum}, shutting down gracefully...")
        self.running = False

    def connect(self):
        """Establish database connection."""
        print(f"[{datetime.now()}] Connecting to database...")
        self.conn = psycopg2.connect(**DB_CONFIG)
        print(f"[{datetime.now()}] Connected to database")

    def load_post_ids(self):
        """Load available post IDs from database and create popularity distribution."""
        cursor = self.conn.cursor()
        cursor.execute("SELECT id FROM posts ORDER BY id")
        self.post_ids = [row[0] for row in cursor.fetchall()]
        cursor.close()

        # Create power law distribution for post popularity
        # Zipf's law: rank proportional to 1/rank^alpha
        n = len(self.post_ids)
        alpha = 1.5  # Power law exponent
        weights = [1.0 / (i + 1) ** alpha for i in range(n)]
        total = sum(weights)
        self.popularity_weights = [w / total for w in weights]

        print(f"[{datetime.now()}] Loaded {len(self.post_ids)} post IDs with power law distribution")

    def generate_event(self) -> tuple:
        """Generate a random view event with realistic popularity distribution."""
        # Use power law distribution for post selection
        post_id = random.choices(self.post_ids, weights=self.popularity_weights, k=1)[0]
        user_id = random.randint(1, 10000)
        viewed_at = datetime.now()
        return (post_id, user_id, viewed_at)

    def insert_event(self, event: tuple):
        """Insert single event into database immediately (streaming)."""
        cursor = self.conn.cursor()
        cursor.execute(
            "INSERT INTO view_events (post_id, user_id, viewed_at) VALUES (%s, %s, %s)",
            event
        )
        self.conn.commit()
        cursor.close()

    def run(self):
        """Run the load generator in streaming mode."""
        try:
            self.connect()
            self.load_post_ids()

            if not self.post_ids:
                print("[ERROR] No posts found in database. Please seed posts first.")
                return

            print(f"[{datetime.now()}] Starting STREAMING load generator: {self.events_per_sec} events/sec")
            print(f"[{datetime.now()}] Events are inserted immediately as they're generated")
            print(f"[{datetime.now()}] Press Ctrl+C to stop\n")

            self.start_time = time.time()
            last_report = self.start_time
            report_interval = 5  # Report stats every 5 seconds

            # Track post view counts for stats
            post_view_counts = {post_id: 0 for post_id in self.post_ids}

            while self.running:
                event_start = time.time()

                # Generate and insert event immediately (streaming!)
                event = self.generate_event()
                post_id, user_id, viewed_at = event

                try:
                    self.insert_event(event)
                    self.total_events += 1
                    post_view_counts[post_id] += 1

                    # Log each event
                    elapsed_total = time.time() - self.start_time
                    current_rate = self.total_events / elapsed_total if elapsed_total > 0 else 0
                    print(f"[{viewed_at.strftime('%H:%M:%S.%f')[:-3]}] Event #{self.total_events}: "
                          f"post_id={post_id}, user_id={user_id} | "
                          f"Rate: {current_rate:.1f} e/s")

                except Exception as e:
                    print(f"[ERROR] Failed to insert event: {e}")

                # Periodic summary report
                current_time = time.time()
                if current_time - last_report >= report_interval:
                    self._print_summary(post_view_counts)
                    last_report = current_time

                # Sleep to maintain target rate
                event_duration = time.time() - event_start
                sleep_time = max(0, (1.0 / self.events_per_sec) - event_duration)
                if sleep_time > 0:
                    time.sleep(sleep_time)

            # Final summary
            print("\n" + "=" * 80)
            self._print_summary(post_view_counts)
            print(f"\n[{datetime.now()}] Shutdown complete. Total events generated: {self.total_events}")

        except Exception as e:
            print(f"\n[{datetime.now()}] Error: {e}")
            raise
        finally:
            if self.conn:
                self.conn.close()
                print(f"[{datetime.now()}] Database connection closed")

    def _print_summary(self, post_view_counts: dict):
        """Print summary statistics."""
        elapsed = time.time() - self.start_time
        avg_rate = self.total_events / elapsed if elapsed > 0 else 0

        # Get top 5 posts
        top_posts = sorted(post_view_counts.items(), key=lambda x: x[1], reverse=True)[:5]

        print("\n" + "-" * 80)
        print(f"[SUMMARY] Total: {self.total_events} events | "
              f"Elapsed: {elapsed:.1f}s | "
              f"Avg Rate: {avg_rate:.2f} e/s")
        print("Top 5 posts this session:")
        for i, (post_id, count) in enumerate(top_posts, 1):
            pct = (count / self.total_events * 100) if self.total_events > 0 else 0
            print(f"  {i}. Post #{post_id}: {count} views ({pct:.1f}%)")
        print("-" * 80 + "\n")

def main():
    """Main entry point."""
    import argparse

    parser = argparse.ArgumentParser(description='Load generator for blog view events')
    parser.add_argument('--rate', type=int, default=DEFAULT_EVENTS_PER_SEC,
                       help=f'Events per second (default: {DEFAULT_EVENTS_PER_SEC})')

    args = parser.parse_args()

    generator = LoadGenerator(events_per_sec=args.rate)
    generator.run()

if __name__ == '__main__':
    main()
