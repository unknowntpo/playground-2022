all: help

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## image/build/manager: build the docker image for manager
.PHONY: image/build/manager
image/build/manager:
	$(MAKE) -C manager -f Makefile image/build/manager

## image/push/manager: push the manager image to dockerhub
.PHONY: image/push/manager
image/push/manager:
	$(MAKE) -C manager -f Makefile image/push/manager

## run/manager: run manager
run/manager:
	$(MAKE) -C manager -f Makefile run/manager