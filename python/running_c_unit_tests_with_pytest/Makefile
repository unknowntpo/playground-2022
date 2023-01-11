all: test

build:
	gcc -shared -o fact.so fact.c

VERBOSE ?= 0

ifneq ($(VERBOSE),0)
	VERBOSE_FLAG = -v
else
	VERBOSE_FLAG =
endif

test: build
	pytest $(VERBOSE_FLAG)

test/in_the_wild: 
	cd in_the_wild && pytest $(VERBOSE_FLAG) ./tests/cunit

