Benchmark result: 

```
pnpm bench
```

```
 ✓ single.bench.js 22277ms
     name                                  hz     min     max    mean     p75     p99    p995    p999     rme  samples
   · Single thread - 1 counter         1.6394  601.04  622.07  609.99  615.76  622.07  622.07  622.07  ±0.89%       10
   · Single thread - 8 promises        1.6146  602.67  704.97  619.36  620.28  704.97  704.97  704.97  ±3.56%       10   slowest
   · Multi threads - 8 worker threads  5.7479  151.32  215.78  173.98  181.82  215.78  215.78  215.78  ±7.23%       10   fastest

 BENCH  Summary

  Multi threads - 8 worker threads - single.bench.js
    3.51x faster than Single thread - 1 counter
    3.56x faster than Single thread - 8 promises

 PASS  Waiting for file changes...
       press h to show help, press q to quit
```