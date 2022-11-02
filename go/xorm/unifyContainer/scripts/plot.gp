reset 
set style fill solid
# set xtics out offset 0, -0.5
set xtics rotate by -45


set size 1, 1
set key right top
set key font ",5"
set term svg

set output 'perf.svg'

set multiplot layout 1, 3 title 'performance comparison' font 'Verdana,15' offset 0.5, 0

set title 'cpu time per operation'
plot 'data/structBinding.csv' u 3:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_noPool.csv' u 3:xtic(1) w linespoints title 'unifyCon_noPool', \
'data/unifyCon_withPool.csv' u 3:xtic(1) w linespoints title 'unifyCon_withPool'


set title 'used bytes per operation'
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'

plot 'data/structBinding.csv' u 5:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_noPool.csv' u 5:xtic(1) w linespoints title 'unifyCon_noPool', \
'data/unifyCon_withPool.csv' u 5:xtic(1) w linespoints title 'unifyCon_withPool'

set title 'number of allocation per operation'
# plot 'result-noPool.csv' u 2:xtic(1) w histograms title 'ns/op'

plot 'data/structBinding.csv' u 7:xtic(1) w linespoints title 'structBinding', \
'data/unifyCon_noPool.csv' u 7:xtic(1) w linespoints title 'unifyCon_noPool', \
'data/unifyCon_withPool.csv' u 7:xtic(1) w linespoints title 'unifyCon_withPool'



unset multiplot