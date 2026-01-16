"""Integration tests for example_hello DAG using Airflow CLI.

Requires docker compose to be running.
"""
from tests.integration.utils.utils import run_airflow_cli


def test_dag_loaded():
    """Test that DAG loads without import errors."""
    result = run_airflow_cli(["airflow", "dags", "list-import-errors"])
    assert result.returncode == 0, f"CLI failed: {result.stderr}"
    # No import errors for example_hello
    assert "example_hello" not in result.stdout, f"DAG has import errors: {result.stdout}"


def test_dag_structure():
    """Test DAG structure via CLI."""
    # Check DAG exists in list
    result = run_airflow_cli(["airflow", "dags", "list", "-o", "plain"])
    assert result.returncode == 0, f"CLI failed: {result.stderr}"
    assert "example_hello" in result.stdout

    # Check tasks exist
    result = run_airflow_cli(["airflow", "tasks", "list", "example_hello"])
    assert result.returncode == 0, f"CLI failed: {result.stderr}"
    assert "hello" in result.stdout
    assert "world" in result.stdout


def test_task_execution():
    """Run full DAG test via CLI in Docker container.

    This bypasses the Airflow 3.1+ dag.test() serialization bug by using CLI.
    Requires docker compose to be running.
    """
    result = run_airflow_cli([
        "airflow", "dags", "test", "example_hello", "2025-01-15"
    ])
    assert result.returncode == 0, f"DAG test failed: {result.stderr}"


def test_task_hello_with_cli():
    """Test individual 'hello' task via CLI."""
    result = run_airflow_cli([
        "airflow", "tasks", "test", "example_hello", "hello", "2025-01-15"
    ])
    assert result.returncode == 0, f"Task test failed: {result.stderr}"
    assert "Hello from Tilt!" in result.stdout


def test_task_world_with_cli():
    """Test individual 'world' task via CLI.

    Note: tasks test runs without dependencies, so 'world' won't receive
    the actual output from 'hello'. It will use default/None for 'msg'.
    """
    result = run_airflow_cli([
        "airflow", "tasks", "test", "example_hello", "world", "2025-01-15"
    ])
    assert result.returncode == 0, f"Task test failed: {result.stderr}"
    assert "World received:" in result.stdout
