# Drone Fleet Management

## Problem
- 1000+ delivery drones
- Track: location, battery, delivery status
- Need: route optimization, low-battery alerts, delivery ETAs

## Concepts
- **Sensor**: wait for external event (drone check-in)
- **Hook**: reusable connection to external system
- **Connection**: store credentials (Airflow UI)
- **Variable**: runtime config values
- **Deferrable Operator**: async wait, free up worker

## Input
```json
{
  "drone_id": "D001",
  "lat": 37.7749,
  "lng": -122.4194,
  "battery_pct": 23,
  "status": "DELIVERING",
  "delivery_id": "DEL_789"
}
```

## Output
```json
{
  "alert": "LOW_BATTERY",
  "drone_id": "D001",
  "action": "RETURN_TO_BASE",
  "nearest_hub": "HUB_SF_01"
}
```

## DAG
```python
with DAG('drone_fleet', schedule='*/1 * * * *'):

    @task
    def fetch_telemetry():
        hook = DroneAPIHook(conn_id='drone_api')
        return hook.get_all_positions()

    @task
    def check_battery(data):
        threshold = Variable.get('battery_threshold', 25)
        return [d for d in data if d['battery_pct'] < threshold]

    @task
    def dispatch_return(low_battery_drones):
        for drone in low_battery_drones:
            send_return_command(drone, find_nearest_hub(drone))

    data = fetch_telemetry()
    low = check_battery(data)
    dispatch_return(low)
```

## Flow
```
Telemetry API → Fetch → Check Battery → Dispatch Return
                     → Update Map     → Calculate ETAs
```

## Connections (Airflow UI)
```
drone_api:    https://api.dronefleet.io  (API key)
hub_db:       postgres://hubs.db         (credentials)
maps_api:     https://maps.google.com    (API key)
```
