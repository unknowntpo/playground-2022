function promise() {
    return new Promise((resolve, reject) => {
        const num = Math.random() > 0.5 ? 1 : 0;
        if (num) {
            resolve('PASS');
        }
        reject('FAIL')
    });
}


const arr = [1, 2, 3, 4, 5]

arr.forEach(e => {
    promise()
        .then((success) => {
            console.log(success);
        }, (fail) => {
            console.log(fail);
        })
})
