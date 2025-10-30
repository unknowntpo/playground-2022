#!/usr/bin/env bash
set -euo pipefail

APP_NAMESPACE=${APP_NAMESPACE:-keel-demo}
KEEL_NAMESPACE=${KEEL_NAMESPACE:-keel}

echo ">>> Watching application pods (Ctrl+C to exit)"
kubectl get pods -n "${APP_NAMESPACE}" -l app=keel-demo -w &
APP_WATCH_PID=$!

trap 'kill ${APP_WATCH_PID} 2>/dev/null || true' EXIT

echo ">>> Streaming Keel controller logs"
kubectl logs -n "${KEEL_NAMESPACE}" deploy/keel -f
