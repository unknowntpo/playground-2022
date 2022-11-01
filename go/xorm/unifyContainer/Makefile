BENCHTIME?=10s
all: bench

bench/StructureBinding:
	go test -benchmem -gcflags="-m" -benchtime=$(BENCHTIME) \
		-bench=BenchmarkContainer/StructureBinding \
		-cpuprofile=data/StructureBinding_cpu.prof \
		-memprofile=data/StructureBinding_mem.prof | tee data/result_StructureBinding.txt

bench/UnifyContainerWithPool:
	go test -benchmem -gcflags="-m" -benchtime=$(BENCHTIME) \
		-bench=BenchmarkContainer/UnifyContainerWithPool \
		-cpuprofile=data/UnifyContainerWithPool_cpu.prof \
		-memprofile=data/UnifyContainerWithPool_mem.prof | tee data/result_UnifyContainerWithPool.txt

bench/UnifyContainerNoPool:
	go test -benchmem -benchtime=$(BENCHTIME) \
		-bench=BenchmarkContainer/UnifyContainerNoPool \
		-cpuprofile=data/UnifyContainerNoPool_cpu.prof \
		-memprofile=data/UnifyContainerNoPool_mem.prof | tee data/result_UnifyContainerNoPool.txt

bench/all:
	go test -benchmem -benchtime=$(BENCHTIME) \
		-bench=BenchmarkContainer \
		-cpuprofile=data/all_cpu.prof \
		-memprofile=data/alL_mem.prof | tee data/result_all.txt

genData/all:
	# @sed -n -E 's/BenchmarkContainer\/(.*)/\1/p' data/result_noPool.txt | column -t -s ',' > data/result-noPool.csv 
	# from make bench/all > bench_result.txt
	sed -n -E 's/.*StructureBinding-(.*)-([0-9]+)/\1/p' bench_result.txt | column -t -s ' ' > data/structBinding.csv
	sed -n -E 's/.*UnifyContainerWithPool-(.*)-([0-9]+)/\1/p' bench_result.txt | column -t -s ' ' > data/unifyCon_withPool.csv
	sed -n -E 's/.*UnifyContainerNoPool-(.*)-([0-9]+)/\1/p' bench_result.txt | column -t -s ' ' > data/unifyCon_noPool.csv



plot:
	gnuplot -c scripts/plot.gp


show/cpu:
	go tool pprof -http=:9988 data/cpu.prof

show/mem:
	go tool pprof -http=:9988 data/mem.prof