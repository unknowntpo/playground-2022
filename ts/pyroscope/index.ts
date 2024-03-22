import * as Pyroscope from '@pyroscope/nodejs';

const slowFunc = async () => {
  await slowFunc2()
}

const slowFunc2 = async () => {
  await concat()
}

async function concat() {
  let str = "";
  for (let i = 0; i < 10000; i++) {
    for (let j = 0; j < 5000; j++) {
      str += "hello";
    }
  }
}


const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms))

function fib(n: number): number {
  if (n < 2) return n;
  return fib(n - 1) + fib(n - 2);
}

async function main() {
  Pyroscope.init({
    serverAddress: 'http://127.0.0.1:4040',
    appName: 'myNodeService'
  });

  console.log("Starting pyroscope");

  Pyroscope.start()

  console.log("Counting fib");

  fib(40);

  console.log("Concatinating string");

  for (let i = 0; i < 1; i++) {
    await slowFunc()
    console.log("Done")
  }

  console.log("Stopping pyroscope");

  Pyroscope.stop();

  console.log("Pyroscope stopped");
}

main()



