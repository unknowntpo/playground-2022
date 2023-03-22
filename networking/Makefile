all: help

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

FILE=large.txt
URL=https://example.com/upload

## genFile: generate large file
genFile:
	dd if=/dev/zero of=large.txt bs=1M count=50

## send/noChunk: send large file without chunked
send/noChunk:
	curl -X POST -F 'file=@./${FILE}' ${URL}


## send/chunk: send large file without chunked
send/chunk:
	curl -X POST -H 'Transfer-Encoding: chunked' --http1.1 -F 'file=@./${FILE}' ${URL}

## send/http2: send large file without chunked
send/http2:
	curl --http2 -X POST -F 'file=@${FILE}' ${URL}

