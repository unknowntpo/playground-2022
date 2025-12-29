# Game Player Event Pipeline

## Problem
- Battle Royale game, 10M daily players
- Events: kills, deaths, purchases, match results
- Need: hourly leaderboards, cheater detection, daily reports

## Concepts
- **DAG**: task dependency graph
- **Operator**: single task (PythonOperator, PostgresOperator)
- **Sensor**: wait for condition (S3KeySensor)
- **XCom**: pass data between tasks
- **Schedule**: `@hourly`, `@daily`

## Input
```json
{
  "event_type": "KILL",
  "player_id": "p123",
  "victim_id": "p456",
  "weapon": "sniper",
  "match_id": "m789"
}
```

## Output
```
| rank | player_id | kills | deaths | kd_ratio |
|------|-----------|-------|--------|----------|
| 1    | p123      | 847   | 102    | 8.30     |
```

## DAG
```python
with DAG('game_leaderboard', schedule='@hourly'):
    wait = S3KeySensor(task_id='wait', bucket_key='events/*.json')
    transform = PythonOperator(task_id='transform', python_callable=process)
    load = PostgresOperator(task_id='load', sql='INSERT INTO leaderboard...')

    wait >> transform >> load
```

## Flow
```
S3 Events → Sensor → Transform → PostgreSQL
                              ↘ Cheater Alert
```
