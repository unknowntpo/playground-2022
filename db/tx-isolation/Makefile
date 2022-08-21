all: pg/up mysql/up

pg/up:
	@echo 'Creating new container...'
	@docker run --rm \
	 --name pg \
	 -v "$(shell pwd)":/pg \
	 -P \
	 -e POSTGRES_PASSWORD=password \
	 -d postgres
	# TODO: Ping postgres for readness
	@sleep 10s
	@echo 'Creating database simple_bank...'
	@docker exec -i pg psql -U postgres -c 'create database simple_bank'
	@echo 'Dumping testdata...'
	@docker exec -i pg psql -U postgres -dsimple_bank -f /pg/setup-pg.sql
pg/connect:
	@docker exec -it pg psql -U postgres -dsimple_bank
pg/down:
	@docker rm -f pg

mysql/up:
	@echo 'Creating new container...' 
	@docker run --rm --name mysql8 -e MYSQL_ROOT_PASSWORD=secret -p 3307:3306 -d mysql:8
	# TODO: Ping mysqlserver for readness
	@sleep 10s
	@echo 'Creating database simple_bank...'
	@docker exec -it mysql8 mysql -uroot -psecret -e 'create database simple_bank'
	@echo 'Dumping testdata...'
	@docker exec -i mysql8 mysql -uroot -psecret -Dsimple_bank < setup-mysql.sql 
mysql/connect:
	@docker exec -it mysql8 mysql -uroot -psecret -Dsimple_bank
mysql/down:
	@docker rm -f mysql8

clean: pg/down mysql/down

.PHONY: pg/up pg/connect pg/down mysql/up mysql/connect/ mysql/down clean