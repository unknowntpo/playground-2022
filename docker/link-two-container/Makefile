build:
	docker build -t pg_upgrade_9.6_14 . --target=final

run:
	# Should see two version of bin tool
	# 14, 9.6
	docker run --rm pg_upgrade_9.6_14 ls /usr/lib/postgresql
