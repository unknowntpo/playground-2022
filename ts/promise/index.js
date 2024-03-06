var printNumber = function (number, delay) {
    return new Promise(function (resolve) {
        setTimeout(function () {
            console.log(number);
            resolve();
        }, delay);
    });
};
var progressBar = function (idx, delay) {
    return new Promise(function (resolve) {
        setTimeout(function () {
            console.log("[".repeat(idx));
            // resolve()
        }, delay);
    });
};
// printNumber(1, 1000)
//     .then(() => printNumber(2, 1000))
//     .then(() => printNumber(3, 1000));
progressBar(1, 1000)
    .then(function () { return progressBar(2, 1000); })
    .then(function () { return progressBar(3, 1000); });
