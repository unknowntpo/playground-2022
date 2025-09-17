console.log('=== Event Loop: Macrotasks vs Microtasks ===\n');

// MACROTASKS (Event Loop Phases):
// - Timer phase: setTimeout, setInterval
// - Poll phase: I/O callbacks, fs.readFile
// - Check phase: setImmediate
// - Close callbacks phase: socket.on('close')

// MICROTASKS (Between phases):
// - process.nextTick (highest priority)
// - Promise.then, async/await
// - queueMicrotask()

function demonstrateEventLoop() {
    console.log('1. Synchronous start');

    // MACROTASKS
    setTimeout(() => console.log('6. Timer phase macrotask'), 0);
    setImmediate(() => console.log('7. Check phase macrotask'));

    // MICROTASKS
    process.nextTick(() => console.log('3. nextTick microtask'));
    Promise.resolve().then(() => console.log('4. Promise microtask'));
    queueMicrotask(() => console.log('5. queueMicrotask'));

    console.log('2. Synchronous end');
}

demonstrateEventLoop();

console.log('\n=== Event Loop Flow ===');
console.log(`
┌─────────────────────────────┐
│    Current Execution        │  ← Synchronous code runs first
└─────────────────────────────┘
              │
              ▼
┌─────────────────────────────┐
│    Microtask Queue          │  ← Process ALL microtasks
│  1. nextTick callbacks      │    (between every phase)
│  2. Promise.then/async      │
│  3. queueMicrotask          │
└─────────────────────────────┘
              │
              ▼
┌─────────────────────────────┐
│    Event Loop Phases        │  ← Macrotasks (one phase at a time)
│  • Timer (setTimeout)       │
│  • Pending callbacks        │
│  • Poll (I/O)              │
│  • Check (setImmediate)     │
│  • Close callbacks          │
└─────────────────────────────┘
              │
              ▼
    (Repeat: check microtasks after each phase)
`);