BENCHTIME?=10s
all: bench
bench/withPool:
	go test -benchmem -gcflags="-m" -benchtime=$(BENCHTIME) -bench=BenchmarkContainer/withPool -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-withPool.txt

bench/noPool:
	go test -benchmem -benchtime=$(BENCHTIME) -bench=BenchmarkContainer/noPool -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-noPool.txt

bench/all:
	go test -benchmem -benchtime=$(BENCHTIME) -bench=BenchmarkContainer -cpuprofile=cpu.prof -memprofile=mem.prof | tee result-all.txt

genData:
	@sed -n -E 's/BenchmarkContainer\/(.*)/\1/p' result-noPool.txt | column -t -s ',' > result-noPool.csv 
	@# @sed -n -E 's/BenchmarkNaivepool\/.*\/(.*)_\w+-[0-9]+/\1/p' go-work.dat | column -t -s ' ' | awk '{print $$1, $$3}' > go-work-final.dat

plot:
	gnuplot -c plot.gp


show/cpu:
	go tool pprof -http=:9988 ./cpu.prof

show/mem:
	go tool pprof -http=:9988 ./mem.prof