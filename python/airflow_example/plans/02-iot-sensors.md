# Smart Factory IoT Monitoring

## Problem
- 500+ sensors (temp, vibration, pressure)
- Push data every 10s
- Need: anomaly detection, alerts before failure

## Concepts
- **TaskFlow API**: `@task` decorator, auto XCom
- **BranchOperator**: conditional paths
- **TriggerRule**: when downstream runs (`all_success`, `one_failed`)
- **SLA**: alert if task exceeds time limit
- **Callback**: `on_failure_callback`

## Input
```json
{
  "sensor_id": "TEMP_CNC_001",
  "value": 89.2,
  "unit": "celsius",
  "threshold_critical": 85.0
}
```

## Output
```json
{"severity": "CRITICAL", "action": "IMMEDIATE_INSPECTION"}
```

## DAG
```python
with DAG('iot_monitor', schedule='*/5 * * * *'):
    @task
    def fetch(): return get_readings()

    @task
    def detect(data): return find_anomalies(data)

    branch = BranchPythonOperator(
        task_id='branch',
        python_callable=lambda: 'alert' if anomalies else 'skip'
    )

    fetch() >> detect() >> branch >> [alert_task, skip_task]
```

## Flow
```
Sensors → Fetch → Detect → Branch
                            ├─ CRITICAL → PagerDuty
                            ├─ WARNING  → Slack
                            └─ OK       → Skip
```
