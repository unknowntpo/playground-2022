#!/usr/bin/env bash
set -euo pipefail

IMAGE_REPO=${IMAGE_REPO:-unknowntpo/keel-test}
TAG=latest
IMAGE="${IMAGE_REPO}:${TAG}"
DEPLOYMENT_FILE=${DEPLOYMENT_FILE:-k8s/keel-demo-deployment.yaml}

if ! command -v docker >/dev/null; then
	echo "docker command not found; install Docker or Docker Desktop first." >&2
	exit 1
fi

echo ">>> Building ${IMAGE}"
docker build -t "${IMAGE}" app

echo ">>> Pushing ${IMAGE}"
docker push "${IMAGE}"

echo ">>> Image ${IMAGE} pushed."
echo "    Ensure your deployment references the desired tag (defaults to 'latest')."
echo "    Keel will detect the new digest and roll the deployment automatically."
