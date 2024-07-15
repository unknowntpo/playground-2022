#/bin/bash

export PGPASSWORD=password
psql -h localhost -p 5433 -Upostgres -c 'SELECT 1'

pg_restore -U $POSTGRES_USER -d dvdrental -f ./dvdrental.zip
