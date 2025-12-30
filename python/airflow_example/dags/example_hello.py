"""
Example DAG for testing hot-reload.
Edit this file and Tilt will sync it automatically.
"""

from airflow.sdk import dag, task
from datetime import datetime


@dag(
    dag_id="hello_tilt",
    start_date=datetime(2024, 1, 1),
    schedule="* * * * *",
    catchup=False,
    tags=["example", "tilt"],
)
def hello_tilt():

    @task
    def hello():
        print("Hello from Tilt!")
        return "done"

    @task
    def world(msg):
        print(f"World received: {msg}")

    result = hello()
    world(result)


hello_tilt()
