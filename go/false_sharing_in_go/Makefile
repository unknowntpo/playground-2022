all: pad/nopad false_sharing

pad/nopad:
	go test -v ./... -bench="Benchmark(No|)Pad"

false_sharing:
	go test -v ./... -bench="BenchmarkFalseSharing"