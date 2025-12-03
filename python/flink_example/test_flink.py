#!/usr/bin/env python3
"""Minimal test to find where Flink hangs."""
import sys
print("Step 1: Starting imports...", flush=True)

from pyflink.table import EnvironmentSettings
print("Step 2: Imported EnvironmentSettings", flush=True)

from pyflink.table import TableEnvironment
print("Step 3: Imported TableEnvironment", flush=True)

print("Step 4: Creating environment settings...", flush=True)
env_settings = EnvironmentSettings.in_streaming_mode()
print("Step 5: Created environment settings", flush=True)

print("Step 6: Creating table environment...", flush=True)
t_env = TableEnvironment.create(env_settings)
print("Step 7: Created table environment - SUCCESS!", flush=True)
