from datetime import datetime

from airflow.sdk import dag, task

from game_analytics.game_datasource import FakeDataSource
from game_analytics.game_event import EventType


@dag(
    dag_id = "game_data_injection",
    # every minute
    schedule= "* * * * *",
    tags=["game"]
)
def game_data_injection():
    @task
    def get_From_kafka():
        game_ids = [f"{i}" for i in range(10)]
        source = FakeDataSource(
            game_ids=["1", "3"], types=[EventType.Kill, EventType.Death]
        )
        start = datetime(2025, 1, 6, 0, 0, 0)
        end = datetime(2026, 1, 7, 0, 0, 0)
        source(start=start, end=end)
        return source
    @task
    def aggregate_by_date():
        return "hello"

    @task
    def write_parquet_to_s3(s: str):
        print(f"got {s} from prev task")

    get_From_kafka()
    res = aggregate_by_date()
    write_parquet_to_s3(res)

game_data_injection()






