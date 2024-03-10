import { Worker } from 'worker_threads';
import path from 'path';
import { fileURLToPath } from 'url';
import v8Profiler from 'v8-profiler-next';
const title = 'good-name';
import fs from 'fs';

// set generateType 1 to generate new format for cpuprofile
// to be compatible with cpuprofile parsing in vscode.
v8Profiler.setGenerateType(1);

// ex. 5 mins cpu profile
v8Profiler.startProfiling(title, true);
setTimeout(() => {
    const profile = v8Profiler.stopProfiling(title);
    profile.export(function (error, result) {
        // if it doesn't have the extension .cpuprofile then
        // chrome's profiler tool won't like it.
        // examine the profile:
        //   Navigate to chrome://inspect
        //   Click Open dedicated DevTools for Node
        //   Select the profiler tab
        //   Load your file
        fs.writeFileSync(`${title}.cpuprofile`, result as string);
        profile.delete();
    });
}, 5 * 1000);

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

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
    for (let i = 0; i < 1000; i++) {
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
