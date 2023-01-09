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

BUILDDIR ?= builddir

## new_builddir: create a build directory for meson.
new_buildir:
	meson setup $(BUILDDIR)

## build: build binaries.
build:
	ninja -C ./$(BUILDDIR)
	
