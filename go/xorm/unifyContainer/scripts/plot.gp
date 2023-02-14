reset 
set style fill solid
# set xtics out offset 0, -0.5
set xtics rotate by -45


# set size 1, 1

set size ratio 2 1, 1
# set size 3, 0.5
set key left top
set key font ",10"
set term svg size 1000, 700

set output 'perf.svg'

set multiplot layout 1, 3 title 'Container Performance Comparison' font 'Verdana,20' offset 0.5, -0.8

set title 'cpu time per operation'
set xlabel 'number of rows'
set ylabel 'ns/op'

# set logscale y 10

plot 'data/structBinding.csv' u 3:xtic(1) w linespoints title 'struct Binding', \
'data/unifyCon_noPool.csv' u 3:xtic(1) w linespoints title 'unifyCon noPool', \
'data/unifyCon_withPool.csv' u 3:xtic(1) w linespoints title 'unifyCon withPool'


set title 'used bytes per operation'
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'
set xlabel 'number of rows'
set ylabel 'Bytes/op'
plot 'data/structBinding.csv' u 5:xtic(1) w linespoints title 'struct Binding', \
'data/unifyCon_noPool.csv' u 5:xtic(1) w linespoints title 'unifyCon noPool', \
'data/unifyCon_withPool.csv' u 5:xtic(1) w linespoints title 'unifyCon withPool'

set title 'number of allocation per operation'
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'
set xlabel 'number of rows'
set ylabel 'allocs/op'
plot 'data/structBinding.csv' u 7:xtic(1) w linespoints title 'struct Binding', \
'data/unifyCon_noPool.csv' u 7:xtic(1) w linespoints title 'unifyCon noPool', \
'data/unifyCon_withPool.csv' u 7:xtic(1) w linespoints title 'unifyCon withPool'



unset multiplot