import Benchmark from "benchmark";
import * as Pyroscope from '@pyroscope/nodejs';

const fib = (i: number): number => {
    if (i < 0) return 0;
    if (i == 0 || i == 1) return i;
    return fib(i-1) + fib(i-2);
}

const fib_iter = (n: number) : number => {
if (n < 0) return 0;
    if (n == 0 || n == 1) return n;
    let pre = 0;
    let cur = 1;
    for (let i = 2; i < n; i++) {
        const nxt = pre + cur;
        pre = cur;
        cur = nxt;
    }
    return cur; 
}

let suite = new Benchmark.Suite('fib', {
    'onStart': setup,
    'onAbort': () => {Pyroscope.stop()},
});

// Adding a test to the suite
suite.add('fib', () => {
    fib(10); // You can change the number to test different inputs
})
suite.add('fib_iter', () => {
    fib_iter(10); // You can change the number to test different inputs
})
.on('cycle', (event: Benchmark.Event) => {
    console.log(String(event.target));
})
.on('complete', function() {
    console.log('Fastest is ' + suite.filter('fastest').map('name'));
})
.run({ 'async': true });

async function setup() {
  Pyroscope.init({
    serverAddress: 'http://localhost:4040',
    appName: 'myNodeService'
  });

  Pyroscope.start()
}