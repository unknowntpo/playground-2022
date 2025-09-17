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


