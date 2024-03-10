import { parentPort } from 'worker_threads';
import fs from 'fs';
import path from 'path';
import v8Profiler from 'v8-profiler-next';
import { fileURLToPath } from 'url';


const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

v8Profiler.setGenerateType(1);

v8Profiler.startProfiling('worker_threads', true);
const start = Date.now();
while (Date.now() - start < 5000) { }
const profile = v8Profiler.stopProfiling('worker_threads');
const title = 'worker_profile';
// const workerProfile = path.join(__dirname, 'worker_threads.cpuprofile');
// fs.existsSync(workerProfile) && fs.unlinkSync(workerProfile);
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

const array = [];

// create heapsnapshot in worker_threads
function createSnapshot(filename: string) {
    const snapshot = v8Profiler.takeSnapshot();
    const file = path.join(__dirname, filename);
    const transform = snapshot.export();
    transform.pipe(fs.createWriteStream(file));
    transform.on('finish', snapshot.delete.bind(snapshot));
}
// Listen for messages from the parent thread
parentPort?.on('message', (task) => {
    console.log(`Worker received task: ${task}`);
    const result = `Processed ${task}`;
    // Send the result back to the parent thread
    parentPort?.postMessage(result);
    for (let i = 0; i < 10000; i++) {
        concat();
        console.log('done concating');
    }
    console.log('creating snapshot');
    createSnapshot('worker_threads.heapsnapshot');
});


function concat() {
    let s = '';
    for (let i = 0; i < 100; i++) {
        s += 'hello';
    }
}
