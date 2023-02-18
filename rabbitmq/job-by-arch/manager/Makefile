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

## venv/activate: activate new venv
venv/activate:
	source $(VENV)/bin/activate

## venv/deactivate: deactive venv
venv/deactivate:
	deactivate

## venv/init: init new venv
venv/init:
	virtualenv $(VENV)
	# https://stackoverflow.com/a/24736236/13919518
	source $(VENV)/bin/activate; \
	pip3 install -r requirements.txt; \

## run/manager: run manager
run/manager:
	python3 manager.py


## clean: cleanup venv
clean:
	-rm -rf $(VENV)