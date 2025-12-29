# Game Matchmaking ELO Pipeline

## Problem
- Competitive FPS, 2M ranked matches/day
- Calculate ELO for 10 players per match
- Update ratings within 5 min of match end

## Concepts
- **Dynamic DAG**: generate DAGs programmatically (per region)
- **Pool**: limit concurrent tasks (prevent DB contention)
- **Priority**: high-rank matches processed first
- **Idempotency**: safe to re-run
- **TaskGroup**: organize related tasks

## Input
```json
{
  "match_id": "m123",
  "winning_team": "A",
  "players": [
    {"player_id": "p1", "team": "A", "elo": 2150, "kills": 24}
  ]
}
```

## Output
```json
{"player_id": "p1", "old_elo": 2150, "new_elo": 2168, "change": +18}
```

## DAG
```python
REGIONS = ['NA', 'EU', 'ASIA']

def create_dag(region):
    with DAG(f'elo_{region}', schedule='*/2 * * * *'):
        @task(pool='db_pool')
        def calc_elo(match_id): ...

        @task
        def apply_rating(result): ...

        matches = fetch_pending(region)
        for m in matches:
            calc_elo(m) >> apply_rating()

for r in REGIONS:
    globals()[f'elo_{r}'] = create_dag(r)
```

## Flow
```
Per Region DAG:
  Fetch Matches → [calc_elo → apply → notify] × N → Update Leaderboard
```

## Pool Config
```
elo_calc_pool:  50 slots (CPU intensive)
db_write_pool:  10 slots (prevent contention)
```
