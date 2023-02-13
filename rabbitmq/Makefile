# Define the Docker images to build
IMAGE_MANAGER = manager:latest
IMAGE_WORKER = worker:latest

all: help

## build-manager: build the manager Docker image
build-manager:
	docker build -t $(IMAGE_MANAGER) -f Dockerfile.manager .

## build-worker: build the worker Docker image
build-worker:
	docker build -t $(IMAGE_WORKER) -f Dockerfile.worker .

## push-manager: push the manager Docker image to the registry
push-manager: build-manager
	docker push $(IMAGE_MANAGER)

## push-worker: push the worker Docker image to the registry
push-worker: build-worker
	docker push $(IMAGE_WORKER)

## deploy: deploy the manager and worker services to Kubernetes using Helm
deploy:
	helm upgrade --install my-release chart/

## undeploy: undeploy the manager and worker services from Kubernetes using Helm
undeploy:
	helm uninstall my-release

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'
