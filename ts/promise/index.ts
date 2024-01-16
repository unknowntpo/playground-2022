const printNumber = (number: number, delay: number): Promise<void> => {
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log(number);
            resolve();
        }, delay);
    });
};

const progressBar = (idx: number, delay: number): Promise<void> => {
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("[".repeat(idx))
            // resolve()
        }, delay)
    })
}

// printNumber(1, 1000)
//     .then(() => printNumber(2, 1000))
//     .then(() => printNumber(3, 1000));

progressBar(1, 1000)
    .then(() => progressBar(2, 1000))
    .then(() => progressBar(3, 1000))