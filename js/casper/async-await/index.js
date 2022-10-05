let runPromise = (someone, timer, success = true) => {
    console.log(`${someone} start running`)
    return new Promise((resolve, reject) => {
        if (success) {
            setTimeout(() => {
                resolve(`${someone} runs ${timer / 1000} seconds (fulfilled)`);
            })
        } else {
            reject(`${someone} failed and fall down (rejected)`)
        }
    })
}

// this code won't block others
runPromise('Ming', 3000).then(someone => {
    console.log('non-blocking', someone)
})

// this code won't block others
runPromise('Antie', 1000).then(someone => {
    console.log('non-blocking', someone)
})