all: help

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

DIRS = 	error_handling

BUILD_DIRS = ${DIRS}


## run: run the program
run:
	go run *.go



## test: run all tests in DIRS
test:

