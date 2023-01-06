FROM postgres:9.6 AS pg_9.6

FROM postgres:14 AS pg_14

FROM postgres:14 AS final

COPY --from=pg_9.6 usr/lib/postgresql/9.6 usr/lib/postgresql/9.6
COPY --from=pg_14 usr/lib/postgresql/14 usr/lib/postgresql/14
