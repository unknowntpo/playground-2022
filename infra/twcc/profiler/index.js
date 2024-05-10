const profiler = require('@google-cloud/profiler');
async function startProfiler() {
for (let i = 0; i < 3; i++) {
    try {
    await profiler.start({
        serviceContext: {
        service: 'your-service',
        version: '1.0.0',
        },
    });
    } catch(e) {
    console.log(`Failed to start profiler: ${e}`);
    }

    // Wait for 1 second before trying again.
    await new Promise(r => setTimeout(r, 1000));
}
}
startProfiler();