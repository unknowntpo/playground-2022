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

## db/up: setup scylladb
db/up:
	# docker run -d -p 9042:9042 --name scylla-helloworld scylladb/scylla
	docker-compose up -d

## db/connect: connect to scylladb
db/connect/%:
	echo '$*'
	docker exec -it helloworld-scylla$* bash

## db/down: teardown db
db/down:
	docker-compose down -v

## run: build and run the program
run/client:
	go run ./main.go

