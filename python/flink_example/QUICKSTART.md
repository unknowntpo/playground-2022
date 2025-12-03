# Flink Blog Trending Posts - Quick Start

## One-Command Startup

```bash
./run.sh all 20
```

This single command:
- ✅ Starts load generator (20 events/sec) in background
- ✅ Starts Flink CDC pipeline in background
- ✅ Starts Streamlit dashboard in foreground
- ✅ All logs saved to `logs/` directory

## What You'll See

After ~60 seconds:
- **Streamlit Dashboard:** http://localhost:8501
- **Flink Web UI:** http://localhost:18081
- **Top 10 trending posts** updating in real-time

## Management Commands

```bash
# Check status
./run.sh status

# View logs
./run.sh logs                  # all logs
./run.sh logs load_generator   # specific log

# Stop everything
./run.sh stop-all              # Python services
./run.sh stop                  # Docker services
```

## Architecture

```
Load Generator (background) → PostgreSQL → Flink CDC → Redis → Dashboard
     ↓ logs/load_generator.log              ↓ logs/flink_cdc.log
```

## Need Help?

- Full documentation: `README.md`
- Troubleshooting: `README.md#troubleshooting`
- Status check: `./run.sh status`

## Complete Commands List

```
./run.sh all [rate]          - Start everything (default: 10 events/sec)
./run.sh load-generator [rate] - Start load generator only
./run.sh flink-cdc           - Start Flink CDC pipeline only
./run.sh dashboard           - Start dashboard only
./run.sh status              - Show service status
./run.sh logs [service]      - Tail logs
./run.sh stop-all            - Stop Python services
./run.sh stop                - Stop Docker services
```

## Quick Test

```bash
# 1. Start everything
./run.sh all 50

# 2. Wait 60 seconds, then open browser:
#    - Dashboard: http://localhost:8501
#    - Flink UI:  http://localhost:18081

# 3. Check status
./run.sh status

# 4. View logs
./run.sh logs

# 5. Stop everything
./run.sh stop-all
```

That's it! 🚀
