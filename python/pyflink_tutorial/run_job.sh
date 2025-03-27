#!/bin/bash
echo 'Starting Flink Python job...'
# source /opt/flink/venv/bin/activate
# PYTHON_CLIENT_EXECUTABLE=$PYPATH
# PYTHON_CLIENT_EXECUTABLE=$PYPATH
# PYFLINK_EXECUTABLE=$PYPATH
# PYTHON_PYTHONPATH=$PYPATH

flink run -m jobmanager:8081 --python hello.py --output /opt/flink/output
