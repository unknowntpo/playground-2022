BENCHTIME?=10s
all: bench
bench/withPool:
	go test -benchmem -benchtime=$(BENCHTIME) -bench=BenchmarkContainer/withPool -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-withPool.txt

bench/noPool:
	go test -benchmem -benchtime=$(BENCHTIME) -bench=BenchmarkContainer/noPool -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-noPool.txt

bench/all:
	go test -benchmem -benchtime=$(BENCHTIME) -bench=BenchmarkContainer -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-all.txt





show/cpu:
	go tool pprof -http=:9988 ./cpu.prof

show/mem:
	go tool pprof -http=:9988 ./mem.prof