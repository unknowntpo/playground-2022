import urllib.request
import json
import time
import sys
import os

API_URL = os.getenv("ARROYO_API_URL", "http://localhost:8000/api/v1/pipelines")


def wait_for_api():
    print(f"Waiting for Arroyo API at {API_URL}...")
    for _ in range(60):
        try:
            req = urllib.request.Request(API_URL)
            with urllib.request.urlopen(req) as response:
                if response.status == 200:
                    print("Arroyo API is ready.")
                    return
        except Exception as e:
            pass
        time.sleep(2)
    print("Arroyo API not reachable.")
    sys.exit(1)


def get_pipelines():
    try:
        req = urllib.request.Request(API_URL)
        with urllib.request.urlopen(req) as response:
            if response.status == 200:
                data = json.loads(response.read().decode("utf-8"))
                return data.get("data", [])
    except Exception as e:
        print(f"Failed to list pipelines: {e}")
        return []
    return []


def stop_and_delete_pipeline(pipeline_id, name):
    print(f"Stopping and deleting existing pipeline: {name} ({pipeline_id})")

    # Stop
    try:
        # Get pipeline details to check status
        req = urllib.request.Request(f"{API_URL}/{pipeline_id}")
        with urllib.request.urlopen(req) as response:
            details = json.loads(response.read().decode("utf-8"))
            if details["stop"] != "stopped":
                # Patch to stop
                data = json.dumps({"stop": "checkpoint"}).encode("utf-8")
                req_stop = urllib.request.Request(
                    f"{API_URL}/{pipeline_id}",
                    data=data,
                    method="PATCH",
                    headers={"Content-Type": "application/json"},
                )
                with urllib.request.urlopen(req_stop) as res:
                    print(f"Stop signal sent for {name}")
                    time.sleep(2)  # Give it a moment
    except Exception as e:
        print(f"Error stopping {name}: {e}")

    # Delete
    try:
        req_del = urllib.request.Request(f"{API_URL}/{pipeline_id}", method="DELETE")
        with urllib.request.urlopen(req_del) as response:
            print(f"Deleted {name}")
    except Exception as e:
        print(f"Error deleting {name}: {e}")


def submit_pipeline(name, sql_file):
    # Check for existing pipelines with this name
    pipelines = get_pipelines()
    for p in pipelines:
        if p["name"] == name:
            stop_and_delete_pipeline(p["id"], name)

    print(f"Submitting pipeline: {name} from {sql_file}")
    if not os.path.exists(sql_file):
        print(f"File not found: {sql_file}")
        return

    with open(sql_file, "r") as f:
        query = f.read()

    print(f"Query content:\n{query}\n----------------")

    payload = {"name": name, "query": query, "parallelism": 1}

    data = json.dumps(payload).encode("utf-8")
    req = urllib.request.Request(
        API_URL, data=data, headers={"Content-Type": "application/json"}
    )

    try:
        with urllib.request.urlopen(req) as response:
            if response.status == 200:
                print(f"Pipeline '{name}' submitted successfully.")
                print(response.read().decode("utf-8"))
    except Exception as e:
        print(f"Failed to submit '{name}': {e}")
        try:
            if hasattr(e, "read"):
                print(e.read().decode("utf-8"))
        except:
            pass


if __name__ == "__main__":
    wait_for_api()
    submit_pipeline("btc-1m-window", "pipelines/1m.sql")
    submit_pipeline("btc-5m-window", "pipelines/5m.sql")
    # Alerts might rely on output topics, but they are independent in this setup (reading from source again? no, source in alerts.sql is KafkaSource<btc-window-1m>)
    # Wait a bit to ensure topics might be created if auto-creation is on, though Arroyo creates them.
    time.sleep(2)
    submit_pipeline("btc-alerts", "pipelines/alerts.sql")
