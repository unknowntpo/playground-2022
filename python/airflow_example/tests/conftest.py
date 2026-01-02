import importlib.util
import sys

# Display where pytest is importing from to catch path shadowing.
print("conftest sys.path[:3] =", sys.path[:3])
print("dags spec:", importlib.util.find_spec("dags"))
print("dags.services spec:", importlib.util.find_spec("dags.services"))

