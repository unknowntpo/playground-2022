const delay = async (duration) => {
    setTimeout(() => {
        console.log("finished")
    }, duration)
}

const main = async () => {
    await delay(4000)
}

main()   