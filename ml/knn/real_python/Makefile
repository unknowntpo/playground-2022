updateReq:
	pipreqs . --force

VENV=./venv

activate:
	source $(VENV)/bin/activate

deactivate:
	deactivate

install:
	virtualenv $(VENV)
	# https://stackoverflow.com/a/24736236/13919518
	source $(VENV)/bin/activate; \
	pip3 install -r requirements.txt; \

clean:
	-rm -rf $(VENV)
    