FROM golang:1.20.4-alpine3.18 as base
COPY main.go .
RUN go build -o /code main.go

FROM golang:1.20.4-alpine3.18
COPY --from=base /code .
ENTRYPOINT ["./code"]
