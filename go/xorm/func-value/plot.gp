reset 
set style fill solid
set ylabel 'ns/op' font 'Verdana, 8'
set key left top
set style histogram clustered gap 1 title offset 0.1,0.25
set term svg
set output 'perf.svg'

set title 'Benchmark Container'
set origin 0.0,0
plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'
