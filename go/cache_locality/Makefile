
bench/all:
	@echo 'Benching all'
	@go test -v ./... -bench=BenchmarkFalseSharing
bench/nopad:
	@echo 'Benching no pad'
	@go test -v ./... -bench=BenchmarkFalseSharing/NoPad
bench/pad:
	@echo 'Benching pad'
	@go test -v ./... -run=BenchmarkFalseSharing/Pad
