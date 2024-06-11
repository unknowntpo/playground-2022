# Apache Ranger Admin runtime Docker image
FROM debian:buster
LABEL maintainer="support@datastrato.com"

ARG RANGER_VERSION=2.4.0
# Multiple plugins can be passed using commas, e.g. `plugin-trino,plugin-hive`
ARG RANGER_PLUGINS=plugin-trino
ENV RANGER_PASSWORD=rangerR0cks!

WORKDIR /root

# RUN --mount=type=cache,target=/var/cache/apt \
RUN apt-get -q update && \
    apt-get install -y -q python python3 && \
    rm -rf /var/lib/apt/lists/*
