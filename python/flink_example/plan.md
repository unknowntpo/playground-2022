# Flink example

Blog app: using flink to get events to find out top 10 trending posts today.

## Arch

Load generator, generating view event on posts in PostgreSQL -> Flink CDC (capture changes) -> PyFlink (window/aggregate) -> streamlit dashboard for top 10 trending posts

## Deploy

Local PostgreSQL run in docker compose file. exported at port 15432,
Local Flink run on host machine
Displayed on streamlit on host machine.

## Design & Implementation Plan

### Phase 1: Database Setup (with CDC enabled)
- [ ] Create docker-compose.yml with PostgreSQL (port 15432)
- [ ] Configure PostgreSQL for CDC (wal_level=logical, max_replication_slots, max_wal_senders)
- [ ] Design schema: `posts` table (id, title, content, created_at)
- [ ] Design schema: `view_events` table (id, post_id, viewed_at, user_id)
- [ ] Create init SQL scripts for tables/indexes
- [ ] Create replication slot for CDC
- [ ] Add .gitignore for postgres data volumes
- [ ] Test PostgreSQL connection and CDC readiness locally

### Phase 2: Load Generator
- [ ] Install dependencies (psycopg2, faker)
- [ ] Create `load_generator.py` script
- [ ] Generate sample blog posts (seed data)
- [ ] Implement view event generator (random posts)
- [ ] Add configurable event rate (events/sec)
- [ ] Add graceful shutdown handling
- [ ] Test load generator locally

### Phase 3: PyFlink CDC Pipeline
- [ ] Install apache-flink, apache-flink-libraries
- [ ] Install flink-cdc-connectors (flink-sql-connector-postgres-cdc)
- [ ] Download/setup PostgreSQL CDC connector JAR
- [ ] Create `flink_pipeline.py`
- [ ] Setup Flink streaming execution environment
- [ ] Configure PostgreSQL CDC source for view_events table
- [ ] Parse CDC events (INSERT operations)
- [ ] Design windowing strategy (5-min tumbling window)
- [ ] Implement aggregation: count views per post_id
- [ ] Implement top-K selection (top 10 posts)
- [ ] Add watermark handling for event time (viewed_at)
- [ ] Test CDC pipeline with load generator

### Phase 4: Streamlit Dashboard
- [ ] Install streamlit, pandas, psycopg2
- [ ] Create `dashboard.py`
- [ ] Design UI layout (title, metrics, top 10 table)
- [ ] Implement real-time data fetching
- [ ] Add auto-refresh (every 5 seconds)
- [ ] Add charts (bar chart for trending posts)
- [ ] Style dashboard (colors, formatting)
- [ ] Test dashboard locally

### Phase 5: Integration & Testing
- [ ] Create startup script (`run.sh`)
- [ ] Test full pipeline: DB -> Generator -> Flink -> Dashboard
- [ ] Verify data flow end-to-end
- [ ] Test with different event rates
- [ ] Check window aggregation correctness
- [ ] Validate top 10 ranking logic
- [ ] Test edge cases (no events, single post)

### Phase 6: Documentation
- [ ] Update README with setup instructions
- [ ] Document architecture diagram
- [ ] Add how to run locally
- [ ] Document dependencies
- [ ] Add troubleshooting section
- [ ] Document environment variables

### Phase 7: Enhancements (Optional)
- [ ] Add monitoring/metrics (Prometheus/Grafana)
- [ ] Add user demographics to events
- [ ] Multi-dimensional trending (by category, region)
- [ ] Add historical trend comparison
- [ ] Implement exactly-once semantics with checkpointing
- [ ] Docker-compose for entire stack (Flink cluster mode)
- [ ] Add CDC for posts table updates (title changes)
- [ ] Implement late data handling