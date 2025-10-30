# Keel Continuous Delivery Demo

This repo contains a minimal application, Docker image, Kubernetes manifests, and helper scripts to demonstrate how [Keel](https://keel.sh) continuously deploys new container tags from Docker Hub into a local Kubernetes cluster.

## Repository layout

- `app/` – tiny Python HTTP service packaged into `unknowntpo/keel-test:<tag>`.
- `k8s/` – namespace, deployment, and service manifests annotated for Keel.
- `scripts/` – helper scripts for installing Keel, deploying the app, building & pushing images, and watching rollouts.
- `plan.md` – high-level implementation plan and spec.

## Prerequisites

- Local Kubernetes cluster (kind, minikube, etc.) configured as current kube-context.
- Docker (with access to push to `unknowntpo/keel-test` on Docker Hub).
- Helm 3.
- `kubectl` CLI.

## Quick start

1. **Install Keel (once per cluster):**
   ```bash
   ./scripts/install-keel.sh
   ```

2. **Deploy the demo workload (namespace, deployment, service):**
   ```bash
   ./scripts/deploy.sh
   ```

3. **Build and push a new image tag (defaults to timestamp tag):**
   ```bash
   ./scripts/build-and-push.sh               # uses current timestamp tag
   ./scripts/build-and-push.sh v0.1.0        # explicit tag
   ```
   The script builds and pushes the tag so Keel can detect the new image digest. Ensure your deployment references the tag you choose (defaults to `latest`).

4. **Watch Keel rollouts (optional):**
   ```bash
   ./scripts/watch-update.sh
   ```
   A successful update shows Keel logs detecting the new image tag and the pod watcher restarting with the fresh tag.

5. **Verify the running version:**
   ```bash
   kubectl port-forward svc/keel-demo -n keel-demo 8080:80
   curl http://localhost:8080
   ```

## Workflow recap

1. Build & push the new tag with `scripts/build-and-push.sh`.
2. Keel polls Docker Hub (per `registry.poll.schedule`) and patches the deployment when it sees the new tag.
3. Kubernetes rolls out the updated pod; the service continues serving traffic.
4. `scripts/watch-update.sh` helps confirm the rollout (Keel logs + pod status).

## Useful environment variables

All scripts provide sensible defaults but can be customized:

| Variable | Default | Purpose |
| --- | --- | --- |
| `IMAGE_REPO` | `unknowntpo/keel-test` | Docker repository pushed by `build-and-push.sh`. |
| `TAG` | timestamp | Tag to build/push (can also be first positional arg). |
| `DEPLOYMENT_FILE` | `k8s/keel-demo-deployment.yaml` | Manifest file updated by the build script. |
| `NAMESPACE` | `keel-demo` | Application namespace for `deploy.sh`. |
| `KEEL_NAMESPACE` | `keel` | Namespace for Keel controller (`install-keel.sh`, `watch-update.sh`). |

## Cleanup

```bash
kubectl delete namespace keel-demo
helm uninstall keel -n keel
kubectl delete namespace keel
```

## Troubleshooting tips

- Ensure Docker is logged in (`docker login`) to push to Docker Hub.
- If Keel does not pick up new tags, inspect its logs: `kubectl logs deploy/keel -n keel`.
- To force Keel reconciliation, restart the Keel pod or change the image tag manually and wait for the poller run.
