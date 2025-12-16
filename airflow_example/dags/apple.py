import logging
from datetime import datetime

from airflow.decorators import dag, task
from pendulum import duration

# Get the 'airflow.task' logger
task_logger = logging.getLogger("airflow.task")


@task
def extract():
    # Use different logging levels
    task_logger.debug(
        "This log is at the DEBUG level"
    )  # Debug logs are often ignored with default settings
    print("This log is also captured using a print statement (stdout)")
    task_logger.info("This is an informational log message")
    task_logger.warning("This log is a warning")
    task_logger.error("This log shows an error!")
    return {"a": 19, "b": 23, "c": 42}


@task
def print_extracted_data(extracted_data):
    """Print the data extracted from the extract task"""
    task_logger.info(f"Extracted data: {extracted_data}")
    print(f"Extracted data: {extracted_data}")
    # Access individual values
    print(f"Value of 'a': {extracted_data['a']}")
    print(f"Value of 'b': {extracted_data['b']}")
    print(f"Value of 'c': {extracted_data['c']}")
    return f"Processed data: {extracted_data}"


@dag(
    "apple",
    start_date=datetime(2025, 12, 16),
    schedule="* * * * *",
    catchup=False,
    dagrun_timeout=duration(minutes=10),
)
def more_logs_dag():
    extracted_data = extract()
    print_extracted_data(extracted_data)


more_logs_dag()
