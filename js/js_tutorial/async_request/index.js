// Use promise.all to do async api call
// https://www.javascripttutorial.net/es6/javascript-promise-all/

const axios = require("axios")

const getRandTime = () => {
    return Math.random() * 1000
}

const delay = async (duration) => {
    setTimeout(() => {
        console.log("finished")
        console.log("duration", duration)
    }, duration)
}

const sleep = async (duration) => {
    await delay(duration)
}

const p1Fn = async () => {
    axios.get('https://google.com')
    // await sleep(getRandTime())
    await sleep(400)
    console.log('The first Fn has been executed');
}

const p2Fn = async () => {
    axios.get('https://google.com')
    // await sleep(getRandTime())
    await sleep(300)
    console.log('The second Fn has been executed');
}

const p3Fn = async () => {
    axios.get('https://google.com')
    // await sleep(getRandTime())
    await sleep(200)


    console.log('The third Fn has been executed');
}



// const p1 = new Promise((resolve, reject) => {
//     // axios.get('https://google.com')
//     // resolve(10)

//     setTimeout(() => {
//         p1Fn()
//         resolve(10);
//     }, getRandTime())
// })


// const p2 = new Promise((resolve, reject) => {
//     // axios.get('https://google.com')
//     // resolve(20)
//     setTimeout(() => {
//         p2Fn()
//         resolve(20);
//     }, getRandTime())
// })


// const p3 = new Promise((resolve, reject) => {
//     // axios.get('https://google.com')
//     // resolve(30)
//     setTimeout(() => {
//         p3Fn()
//         resolve(30);
//     }, getRandTime())
// })

// Promise.all([p1, p2, p3]).then((results) => {
//     const total = results.reduce((p, c) => p + c);

//     console.log(`Results: ${results}`);
//     console.log(`Total: ${total}`);
// })



async function main() {
    console.log("Using setTimeout with 0 timeout")

    setTimeout(() => {
        p1Fn()
    }, 0)

    setTimeout(() => {
        p2Fn()
    }, 0)

    setTimeout(() => {
        p3Fn()
    }, 0)
}

main()