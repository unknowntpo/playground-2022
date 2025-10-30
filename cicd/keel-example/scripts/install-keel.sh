#!/usr/bin/env bash
set -euo pipefail

RELEASE_NAME=${RELEASE_NAME:-keel}
NAMESPACE=${KEEL_NAMESPACE:-keel}
HELM_REPO_NAME=${HELM_REPO_NAME:-keel-charts}
HELM_REPO_URL=${HELM_REPO_URL:-https://charts.keel.sh}

echo ">>> Ensuring Helm repository ${HELM_REPO_NAME} is available"
helm repo add "${HELM_REPO_NAME}" "${HELM_REPO_URL}" >/dev/null 2>&1 || true
helm repo update "${HELM_REPO_NAME}" >/dev/null

echo ">>> Creating namespace ${NAMESPACE} if needed"
kubectl create namespace "${NAMESPACE}" --dry-run=client -o yaml | kubectl apply -f -

echo ">>> Installing/Upgrading Keel release ${RELEASE_NAME}"
helm upgrade --install "${RELEASE_NAME}" "${HELM_REPO_NAME}/keel" \
  --namespace "${NAMESPACE}" \
  --create-namespace \
  --set service.type=ClusterIP \
  --set auth.anonymous.enabled=true \
  --set registry.poll.enabled=true \
  --set registry.poll.schedule="*/1 * * * *"

echo ">>> Keel installation complete"
echo "    View pods with: kubectl get pods -n ${NAMESPACE}"
