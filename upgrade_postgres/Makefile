all: help

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

IMG ?= pg_upgrade_9.6_14

## build/image: build image of container with two version of pg bins
build/image:
	docker build -t $(IMG) . --target=final

## db/init: start original 9.6 postgres instance
db/init:
	docker-compose up -d db

## db/adddata: add some data to postgres instance
db/adddata:
	docker-compose exec db psql -U postgres -d postgres -c 'select generate_series(1,100) into foo;'
## db/connect: connect to postgres instanct
db/connect:
	docker-compose exec db psql -U postgres -d postgres

## upgrade/connect: connect to postgres instanct
upgrade/connect:
	docker-compose exec upgrade gosu postgres bash



## db/upgrade: start pg_upgrade tool
db/upgrade:
	docker-compose up -d upgrade
	docker-compose exec upgrade gosu postgres bash -c 'cd /home/postgres && echo hello'
	 docker-compose exec upgrade gosu postgres bash -c 'cd /home/postgres && \
		$$PGBINNEW/pg_upgrade \
		--old-bindir=$$PGBINOLD/ \
		--new-bindir=$$PGBINGNEW/ \
		--old-datadir=$$PGBINOLD \
		--new-datadir=$$PGDATANEW --check'
 

## clean: clean artifects 
clean:
	-rm -rf ./postgresql
	docker-compose rm -f upgrade db

