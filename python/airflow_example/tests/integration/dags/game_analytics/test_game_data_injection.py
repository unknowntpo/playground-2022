import subprocess
from pathlib import Path

PROJECT_ROOT = Path(__file__).parent.parent.parent.parent  # tests/integration/dags -> project root
DAGS_FOLDER = PROJECT_ROOT / "dags"

def test_dag_with_cli():
    """Run full DAG test via CLI in Docker container.

    This bypasses the Airflow 3.1+ dag.test() serialization bug by using CLI.
    Requires docker compose to be running.
    """
    result = subprocess.run(
        [
            "docker", "compose", "exec", "-T", "airflow",
            "airflow", "dags", "test", "get_from_kafka", "2025-01-15"
        ],
        capture_output=True,
        text=True,
        cwd=str(PROJECT_ROOT),
    )

    print(result.stdout)
    if result.stderr:
        print(f"STDERR: {result.stderr}")

    assert result.returncode == 0, f"DAG test failed: {result.stderr}"
