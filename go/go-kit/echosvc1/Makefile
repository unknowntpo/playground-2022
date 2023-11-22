all: help

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

IMG=unknowntpo/echosvc1

## image/build: build dockerfile
image/build:
	@docker build -t $(IMG) .

## image/push: push the config image to dockerhub
.PHONY: image/push
image/push:
	@docker push $(IMG):latest

## skaffold/init: init skaffold
skaffold/init:
	@skaffold init -f skaffold.yml

## skaffold/build: build artifect
skaffold/build:
	@skaffold build

## skaffold/dev: dev skaffold
skaffold/dev:
	@skaffold dev


## skaffold/deploy: deploy skaffold
skaffold/deploy:
	@skaffold deploy --images $(IMG)

