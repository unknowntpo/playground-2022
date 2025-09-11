setTimeout(()=> {
    console.log(`timeout`);
});

setImmediate(() => {
     console.log(`immediate`);
});
