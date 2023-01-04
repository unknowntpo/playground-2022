updateReq:
	pipreqs . --force

VENV=./venv

install:
	virtualenv $(VENV)
	# https://stackoverflow.com/a/24736236/13919518
	source $(VENV)/bin/activate; \
	pip3 install -r requirements.txt; \

clean:
	-rm -rf $(VENV)
    