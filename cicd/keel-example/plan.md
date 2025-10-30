# Keel example

An example demo of CD workflow with keel.

- manually push new image `unknowntpo/keel-test`.
- keel should update image in local k8s cluster.

## Spec

- **Container image**: Sample HTTP API built from `app/Dockerfile` that returns version info (`APP_MESSAGE` + `IMAGE_TAG`).
- **Registry**: Docker Hub repository `unknowntpo/keel-test` assumed; scripts tag/push `unknowntpo/keel-test:<version>`.
- **Kubernetes deployment**: `k8s/keel-demo-deployment.yaml` with Keel annotations (`keel.sh/policy: force`, `keel.sh/trigger: poll`, `keel.sh/match-tag: "true"`) so Keel tracks the exact tag used in the deployment.
- **Service**: `k8s/keel-demo-service.yaml` exposes the pod on ClusterIP `8080`; port mapping ready for port-forwarding.
- **Keel installation**: Helm-based setup via `scripts/install-keel.sh` (namespace `keel`, anonymous auth for local testing, docker registry polling enabled).
- **Automation scripts**:
  - `scripts/build-and-push.sh <tag>` builds/pushes the Docker image with tag and updates the deployment manifest.
  - `scripts/deploy.sh` applies Kubernetes manifests and waits for rollout.
  - `scripts/watch-update.sh` follows pod rollout/logs to confirm Keel triggered updates.
