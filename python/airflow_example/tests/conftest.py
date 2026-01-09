import os
import tempfile

# Use temp file SQLite for tests (needed for any DB-based Airflow operations)
_test_db = tempfile.NamedTemporaryFile(suffix=".db", delete=False)
os.environ["AIRFLOW__DATABASE__SQL_ALCHEMY_CONN"] = f"sqlite:///{_test_db.name}"
os.environ["AIRFLOW__CORE__LOAD_EXAMPLES"] = "False"

