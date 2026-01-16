import subprocess
from pathlib import Path

# tests/integration/utils/utils.py -> project root (4 levels up)
PROJECT_ROOT = Path(__file__).parent.parent.parent.parent


def run_airflow_cli(cmd: list[str]) -> subprocess.CompletedProcess:
    """Helper to run airflow CLI commands in docker container."""
    full_cmd = ["docker", "compose", "exec", "-T", "airflow"] + cmd
    result = subprocess.run(
        full_cmd,
        capture_output=True,
        text=True,
        cwd=str(PROJECT_ROOT),
    )
    print(result.stdout)
    if result.stderr:
        print(f"STDERR: {result.stderr}")
    return result
