from pathlib import Path

import pytest
from airflow.models import DagBag

PROJECT_ROOT = Path(__file__).parent.parent.parent.parent  # tests/integration/dags -> project root
DAGS_FOLDER = PROJECT_ROOT / "dags"

@pytest.fixture
def dagbag():
    dagbag = DagBag(dag_folder=DAGS_FOLDER, include_examples=False)
    assert len(dagbag.import_errors) == 0, f"DAG import errors: {dagbag.import_errors}"
    return dagbag

def test_dag_loaded(dagbag):
    """Test that the specific DAG is loaded correctly"""
    print(dagbag.dags)
    print(PROJECT_ROOT)
    dag = dagbag.dags.get('example_hello')  # avoid DB query
    assert dag is not None
    assert dag.dag_id == 'example_hello'

def test_dag_structure(dagbag):
    dag = dagbag.dags.get("example_hello")  # avoid DB query

    assert len(dag.tasks) == 2

    task_ids = [task.task_id for task in dag.tasks]
    assert "hello" in task_ids
    assert "world" in task_ids

    world_task = dag.get_task("world")
    upstream_list = [t.task_id for t in world_task.upstream_list]
    assert "hello" in upstream_list

def test_task_execution(dagbag):
    """Test task logic by calling underlying Python functions.

    Note: dag.test() doesn't work in pytest due to Airflow 3.1+ serialization bug.
    See: https://github.com/apache/airflow/issues/56657
    Use `airflow dags test example_hello` from CLI for full integration test.
    """
    dag = dagbag.dags.get('example_hello')

    hello_task = dag.get_task("hello")
    world_task = dag.get_task("world")

    # Test the actual Python functions
    hello_result = hello_task.python_callable()
    assert hello_result == "done"

    world_result = world_task.python_callable(hello_result)
    assert world_result == "done"


