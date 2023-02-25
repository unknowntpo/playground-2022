# check installation of githooks and display help message when typing make
all: help

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## deps/update: update requirement.txt
deps/update:
	pipreqs . --force

## deps/install: install requirement.txt
deps/install:
	pip3 install -r requirements.txt

## run/manager: run manager
run/manager:
	python3 manager.py

## image/build/manager: build the docker image for manager
.PHONY: image/build/manager
image/build/manager:
	@DOCKER_BUILDKIT=1 docker build \
	    --file Dockerfile \
	    --build-arg VERSION=${git_description} \
	    --build-arg BUILDKIT_INLINE_CACHE=1 \
	    --network host \
	    --tag unknowntpo/job-manager:latest .

## image/push/manager: push the manager image to dockerhub
.PHONY: image/push/manager
image/push/manager:
	@docker push unknowntpo/job-manager:latest

## clean: cleanup venv
clean:
	-rm -rf $(VENV)