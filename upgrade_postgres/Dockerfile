# Ref: https://github.com/tianon/docker-postgres-upgrade/blob/master/9.6-to-14/docker-upgrade
FROM postgres:9.6 AS pg_9.6

FROM postgres:14 AS pg_14

FROM postgres:14 AS final

# COPY --from=pg_9.6 usr/lib/postgresql/9.6 usr/lib/postgresql/9.6
COPY --from=pg_14 usr/lib/postgresql/14 usr/lib/postgresql/14

ENV PGBINOLD /usr/lib/postgresql/9.6/bin
ENV PGBINNEW /usr/lib/postgresql/14/bin

ENV PGDATAOLD /var/lib/postgresql/data
ENV PGDATANEW /var/lib/postgresql/14/data

RUN mkdir -p "$PGDATAOLD" "$PGDATANEW" && \
	chown -R postgres:postgres /var/lib/postgresql/*

RUN mkdir -p /home/postgres && \
	chown -R postgres:postgres /home/postgres

CMD ["sh", "-c", "while true; do echo 1; sleep 1; done"]
