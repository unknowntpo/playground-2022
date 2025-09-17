import fs from 'node:fs';
import net from 'node:net';

console.log('=== Start ===');

// Timer phase
setTimeout(() => {
    console.log('Timer phase: setTimeout');
    process.nextTick(() => console.log('nextTick after Timer phase'));
}, 0);

setTimeout(() => {
     process.nextTick(() => console.log('nextTick after Timer phase 2'));
}, 0);

// Poll phase (I/O)
fs.readFile(new URL(import.meta.url).pathname, () => {
    console.log('Poll phase: I/O callback');
    process.nextTick(() => console.log('nextTick after Poll phase'));
});

// Check phase
setImmediate(() => {
    console.log('Check phase: setImmediate');
    process.nextTick(() => console.log('nextTick after Check phase'));
});

// Close callbacks phase
const server = net.createServer();
server.listen(0, () => {
    server.close(() => {
        console.log('Close callbacks phase: server close');
        process.nextTick(() => console.log('nextTick after Close phase'));
    });
});

console.log('=== End of synchronous code ===');

console.log('\n=== API Consistency Example ===');

function badAsyncAPI(useCache: boolean, callback: (data: string) => void) {
    if (useCache) {
        // Sync return - inconsistent!
        console.log('Returning cached data synchronously');
        callback('cached data');
    } else {
        // Async return
        console.log('Reading file asynchronously');
        setTimeout(() => callback('file data'), 10);
    }
}

function goodAsyncAPI(useCache: boolean, callback: (data: string) => void) {
    if (useCache) {
        console.log('Returning cached data with nextTick');
        process.nextTick(() => callback('cached data'));
    } else {
        console.log('Reading file asynchronously');
        setTimeout(() => callback('file data'), 10);
    }
}

setTimeout(() => {
    console.log('--- Testing bad API ---');
    console.log('Before calling badAsyncAPI with cache');
    badAsyncAPI(true, (data) => console.log('BAD API Callback received:', data));
    console.log('After calling badAsyncAPI with cache');
}, 100);

setTimeout(() => {
    console.log('\n--- Testing good API ---');
    console.log('Before calling goodAsyncAPI with cache');
    goodAsyncAPI(true, (data) => console.log('GOOD API Callback received:', data));
    console.log('After calling goodAsyncAPI with cache');
}, 200);
