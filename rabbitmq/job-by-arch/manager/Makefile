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

## updateReq: update requirement.txt
updateReq:
	pipreqs . --force

VENV=./venv

## activate: activate new venv
activate:
	source $(VENV)/bin/activate

## deactivate: deactive venv
deactivate:
	deactivate

## install: install required deps
install:
	virtualenv $(VENV)
	# https://stackoverflow.com/a/24736236/13919518
	source $(VENV)/bin/activate; \
	pip3 install -r requirements.txt; \

## clean: cleanup venv
clean:
	-rm -rf $(VENV)