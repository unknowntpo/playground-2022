"""
Example DAG for testing hot-reload.
Edit this file and Tilt will sync it automatically.
"""
from airflow import DAG
from airflow.decorators import task
from datetime import datetime

with DAG(
    dag_id='hello_tilt',
    start_date=datetime(2024, 1, 1),
    schedule='@daily',
    catchup=False,
    tags=['example', 'tilt'],
) as dag:

    @task
    def hello():
        print("Hello from Tilt!")
        return "done"

    @task
    def world(msg):
        print(f"World received: {msg}")

    world(hello())
