const axios = require('axios')

const baseURL = 'https://httpbin.org/'

// axios.get(baseURL + "get").then(resp => {
//     console.log(resp.status)
// })


async function doGetRequest() {
    let res = await axios.get(baseURL + "get")

    console.log(Object.keys(res));

    console.log(res.status);
    let data = res.data;
    console.log(data);
}

async function doPostRequest() {
    const config = {
        method: 'post',
        url: baseURL + "post"
    }

    let res = await axios(config)

    console.log(Object.keys(res));

    console.log(res.status);
    let data = res.data;
    console.log(data);
}



// doGetRequest();
doPostRequest();