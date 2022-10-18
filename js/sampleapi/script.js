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
    const menu = document.querySelector("#menu")

    // https://www.javascripttutorial.net/javascript-dom/javascript-appendchild/
    data.forEach((e) => {
        console.log(e.title)
        let ul = document.createElement("ul")
        ul.innerText = e.title
        menu.appendChild(ul)
    })
}