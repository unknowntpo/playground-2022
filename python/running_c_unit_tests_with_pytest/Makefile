all: test

build:
	gcc -shared -o fact.so fact.c

test: build
	pytest

