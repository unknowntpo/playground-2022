console.log(`Calling Sync function`);

setImmediate(() => console.log(`in setImmediate`));
process.nextTick(() => console.log(`in nextTick`));
