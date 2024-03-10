import { Worker } from 'worker_threads';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Create a new worker
const worker = new Worker(path.resolve(__dirname, 'worker.js'));

// Send a task to the worker
worker.postMessage('Hello, Worker!');

// Listen for messages from the worker
worker.on('message', (result) => {
  console.log(`Main thread received result: ${result}`);
  for (let i = 0; i < 10000; i++) {
    concat();
    console.log('done concating');
  }
  sleep(1000000);
});

const sleep = async (ms: number) => {
  return new Promise<void>((resolve) => {
    setTimeout(() => resolve(), ms)
  })
}

function concat() {
  let s = '';
  for (let i = 0; i < 10000000; i++) {
    s += 'hello';
  }
}

// Listen for any errors that occur in the worker thread
worker.on('error', (error) => {
  console.error('Worker error:', error);
});

// Listen for when the worker stops
worker.on('exit', (code) => {
  if (code !== 0)
    console.error(`Worker stopped with exit code ${code}`);
});
