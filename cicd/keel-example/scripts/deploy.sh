#!/usr/bin/env bash
set -euo pipefail

NAMESPACE=${NAMESPACE:-keel-demo}

echo ">>> Applying namespace"
kubectl apply -f k8s/namespace.yaml

echo ">>> Applying application manifests"
kubectl apply -f k8s/keel-demo-deployment.yaml
kubectl apply -f k8s/keel-demo-service.yaml

echo ">>> Waiting for deployment rollout"
kubectl rollout status deployment/keel-demo -n "${NAMESPACE}" --timeout=120s

echo ">>> Current pods"
kubectl get pods -n "${NAMESPACE}" -o wide
