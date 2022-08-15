all: mysql/setup

pg/setup:

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