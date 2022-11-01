reset 
set style fill solid
# set xtics out offset 0, -0.5
set xtics rotate by -45
set size 1, 1
set ylabel 'ns/op'
set key left top
set term svg
set output 'perf.svg'

set multiplot layout 1, 3 title 'performance comparison' font 'Verdana,15' offset 0.5,-10

set title 'cpu time per operation'
set origin 0.0,0
plot 'data/structBinding.csv' u 3:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_withPool.csv' u 3:xtic(1) w linespoints title 'unifyCon_withPool'

set title 'used bytes per operation'
set origin 0.33,0
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'

plot 'data/structBinding.csv' u 5:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_withPool.csv' u 5:xtic(1) w linespoints title 'unifyCon_withPool'

set title 'number of allocation per operation'
set origin 0.66,0
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'

plot 'data/structBinding.csv' u 7:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_withPool.csv' u 7:xtic(1) w linespoints title 'unifyCon_noPool'


unset multiplot