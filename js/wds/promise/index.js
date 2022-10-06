function promise() {
    return new Promise((resolve, reject) => {
        const num = Math.random() > 0.5 ? 1 : 0;
        if (num) {
            resolve('PASS');
        }
        reject('FAIL')
    });
}

promise()
    .then((success) => {
        console.log(success);
    }, (fail) => {
        console.log(fail);
    })