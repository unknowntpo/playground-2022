all: pad_nopad false_sharing

pad_nopad:
	go test -v ./... -bench="Benchmark(No|)Pad"

false_sharing:
	go test -v ./... -bench="BenchmarkFalseSharing"