FROM golang:1.20.4-alpine3.18 as base
RUN apk add --no-cache curl \
    git \
    bash \
    make \
    build-base
WORKDIR /app

COPY . .

RUN go mod download

RUN make build/producer
RUN make build/consumer

FROM golang:1.20.4-alpine3.18 AS consumer
COPY --from=base /app/out/consumer .
ENTRYPOINT ["./consumer"]

FROM golang:1.20.4-alpine3.18 AS producer
COPY --from=base /app/out/producer .
ENTRYPOINT ["./producer"]
