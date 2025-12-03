#!/bin/bash
set -e

echo "=========================================="
echo "Starting Flink Application Cluster"
echo "=========================================="

# Start Flink standalone cluster
echo "[INFO] Starting Flink JobManager and TaskManager..."
/opt/flink/bin/start-cluster.sh

# Wait for cluster to be ready
echo "[INFO] Waiting for Flink cluster to be ready..."
sleep 10

# Check if WebUI is accessible
until curl -s http://localhost:8081 > /dev/null; do
    echo "[INFO] Waiting for Flink WebUI..."
    sleep 2
done

echo "[✓] Flink cluster is ready!"
echo "[✓] WebUI: http://localhost:8081"
echo ""

# Submit PyFlink job
echo "[INFO] Submitting PyFlink CDC job..."
/opt/flink/bin/flink run \
    --jobmanager localhost:8081 \
    --python /opt/flink/job/flink_cdc_simple.py \
    --detached

echo "[✓] Job submitted successfully!"
echo ""
echo "=========================================="
echo "Flink Application is running"
echo "=========================================="
echo "WebUI: http://localhost:8081"
echo "Logs: /opt/flink/log/"
echo ""
echo "Tailing logs..."
echo ""

# Keep container alive and tail logs
tail -f /opt/flink/log/*.log
