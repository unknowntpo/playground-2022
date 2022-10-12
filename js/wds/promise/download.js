function download() {
    return new Promise((resolve, reject) => {
        const respCode = doDownload()
        if (respCode != 200) {
            reject(`Fail to download, resp: ${respCode}`)
        } else {
            resolve("OK")
        }
    })

}

function doDownload() {
    return Math.random() > 0.5 ? 200 : 500
}

for (let i = 0; i < 10; i++) {
    download().then((success) => console.log("SUCCESS"), (rejected) => console.log("FAIL"))
}
