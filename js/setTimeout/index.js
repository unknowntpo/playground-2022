// function download(i) {
//     setTimeout(() => { console.log(`index: ${i}`) }, 0)
// }

// const cnt = 5

// for (let i = 0; i < cnt; i++) {
//     download(i)
// }

async function viewSite(url) {
    console.log('Processing..' + url);
    try {
        const res = await axios.get(url);
        return res.data;
    } catch (error) {
        // do something or throw error;
    }
}

async function collectAllSitesData() {
    const url1 = "https://www.google.com/"
    const url2 = "https://www.facebook.com/"

    const results = await Promise.all([viewSite(url1), viewSite(url2)]);
    console.log(results)

    // do something with results here, not after call of collectAllSitesData()
}

collectAllSitesData();