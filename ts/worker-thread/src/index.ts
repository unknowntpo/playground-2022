import { Worker } from 'worker_threads';
import path from 'path';
import { fileURLToPath } from 'url';
import profiler from '@google-cloud/profiler';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);


await profiler.start({
  projectId: 'web-service-design',
  serviceContext: {
    service: 'web-service-design-cloud-profiler-worker-thread',
    version: '1.3.0',
  },
  sourceMapSearchPath: ['.'],
});


// Create a new worker
const worker = new Worker(path.resolve(__dirname, 'worker.js'));

// Send a task to the worker
worker.postMessage('Hello, Worker!');

// Listen for messages from the worker
worker.on('message', async (result) => {
  console.log(`Main thread received result: ${result}`);
  for (let i = 0; i < 10000; i++) {
    concat();
    await sleep(10);
  }
  console.log('wait for worker to be terminated')
  await worker.terminate();
});

const sleep = async (ms: number) => {
  return new Promise<void>((resolve) => {
    setTimeout(() => resolve(), ms)
  })
}

function concat() {
  let s = '';
  for (let i = 0; i < 100000; i++) {
    s += 'hello';
  }
}

// Listen for any errors that occur in the worker thread
worker.on('error', (error) => {
  console.error('Worker error:', error);
});

// Listen for when the worker stops
worker.on('exit', (code) => {
  console.log(`Worker stopped with exit code ${code}`);
});
