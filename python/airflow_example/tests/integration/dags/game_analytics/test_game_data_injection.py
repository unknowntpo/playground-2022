import subprocess
from pathlib import Path

PROJECT_ROOT = Path(__file__).parent.parent.parent.parent  # tests/integration/dags -> project root
DAGS_FOLDER = PROJECT_ROOT / "dags"