from datetime import datetime

from airflow.sdk import dag, task

from game_analytics.game_datasource import FakeDataSource
from game_analytics.game_event import EventType, Event


@dag(
    dag_id = "game_data_injection",
    # every minute
    schedule= "* * * * *",
    tags=["game"]
)
def game_data_injection():
    @task
    async def get_from_kafka():
        game_ids = [f"{i}" for i in range(10)]
        source = FakeDataSource(
            game_ids=game_ids, types=[EventType.Kill, EventType.Death], num_events=100,
        )
        start = datetime(2025, 1, 6, 0, 0, 0)
        end = datetime(2026, 1, 7, 0, 0, 0)
        await source(start=start, end=end)
        return [e.model_dump(mode="json") for e in source]

    @task
    def aggregate_by_date(events: list[Event]):
        print(events)
        return "ok"

    @task
    def write_parquet_to_s3(s: str):
        print(f"got {s} from prev task")

    events = get_from_kafka()
    res = aggregate_by_date(events)
    write_parquet_to_s3(res)

# TODO: move into dag function
async def consume_events():
    source = FakeDataSource(
        game_ids=["1", "3"], types=[EventType.Kill, EventType.Death], num_events=10
    )
    start = datetime(2025, 1, 6, 0, 0, 0)
    end = datetime(2025, 1, 7, 0, 0, 0)
    await source(start=start, end=end)
    return list(source)


game_data_injection()




