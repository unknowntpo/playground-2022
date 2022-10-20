all: build

BIN = adder

build:
	go build -gcflags=-l -o $(BIN) main.go

trace:
	sudo bpftrace -e 'uprobe:$(shell pwd)/$(BIN):main.add { printf("hello\n"); }'