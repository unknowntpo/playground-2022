import { parentPort } from 'worker_threads';
import fs from 'fs';
import path from 'path';

const array = [];

// Listen for messages from the parent thread
parentPort?.on('message', (task) => {
  console.log(`Worker received task: ${task}`);
  const result = `Processed ${task}`;
  // Send the result back to the parent thread
  parentPort?.postMessage(result);
  for (let i = 0; i < 10000; i++) {
    concat();
  }
  console.log('done concating');
});


function concat() {
  let s = '';
  for (let i = 0; i < 1000; i++) {
    s += 'hello';
  }
}
