all: build

BIN = adder

build:
	go build -gcflags=-l -o $(BIN) main.go

trace:
	sudo bpftrace trace.bt