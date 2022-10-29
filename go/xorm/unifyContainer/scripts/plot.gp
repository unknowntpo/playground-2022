reset 
set style fill solid
# set xtics offset 0, graph 0
set size 1, 1
set ylabel 'ns/op' font 'Verdana, 8'
set key left top
set term svg
set output 'perf.svg'

set title 'Benchmark Container'
set origin 0.0,0
plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'
