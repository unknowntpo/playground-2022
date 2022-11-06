// Use promise.all to do async api call
// https://www.javascripttutorial.net/es6/javascript-promise-all/

const getRandTime = () => {
    return Math.random() * 1000
}

const p1 = new Promise((resolve, reject) => {
    setTimeout(() => {
        console.log('The first promise has resolved');
        resolve(10);
    }, getRandTime())
})

const p2 = new Promise((resolve, reject) => {
    setTimeout(() => {
        console.log('The second promise has resolved');
        resolve(20);
    }, getRandTime())
})

const p3 = new Promise((resolve, reject) => {
    setTimeout(() => {
        console.log('The third promise has resolved');
        resolve(30);
    }, getRandTime())
})

Promise.all([p1, p2, p3]).then((results) => {
    const total = results.reduce((p, c) => p + c);

    console.log(`Results: ${results}`);
    console.log(`Total: ${total}`);
})
