all: help

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

COPY_IMG ?= pg_upgrade_9.6_14

## build/image: build image of container with two version of pg bins
build/image:
	docker build -t $(COPY_IMG) . --target=final

## get_bin: get bin of pg_14 and pg_9.6 
get_bin: build/image
	docker run --name copycon -d --rm $(COPY_IMG) 
	docker cp copycon:/usr/lib/postgresql/ .
	docker rm -f copycon


## clean: clean artifects 
clean:
	-rm -rf ./postgresql

