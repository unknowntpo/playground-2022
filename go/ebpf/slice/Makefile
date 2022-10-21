all: build

BIN = slice

build:
	go build -gcflags=-l -o $(BIN) main.go

trace:
	# Ref: https://cloud.tencent.com/developer/article/1918230
	sudo bpftrace -e 'uprobe:$(shell pwd)/$(BIN):runtime.growslice { printf("old cap: %d, desired cap: %d\n",reg("di"), reg("si")); }'