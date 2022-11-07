const axios = require('axios')

const baseURL = 'https://httpbin.org/'

axios.get(baseURL + "get").then(resp => {
    console.log(resp.status)
})