const baseURL = 'https://api.sampleapis.com/coffee/hot';
fetch(baseURL)
    .then(resp => {
        console.log(resp)
        return resp.json()
    })
    .then(data => {
        console.log(data)
        displayData(data)
    })

function displayData(data) {
    data.forEach((e) => console.log(e.title))
    document.querySelector("pre").innerHTML = JSON.stringify(data, null, 2);
}