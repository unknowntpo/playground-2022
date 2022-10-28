const obj = {
    "name": "coffee",
    "age": 30,
    "location": "Taiwan"
}

const menu = document.querySelector("#menu")
listProperties(obj)

function listProperties(obj) {
    for (let p in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, p)) {
            let n = document.createElement("ul")
            n.innerText = `${p}: ${obj[p]}`
            menu.appendChild(n)
        }
    }
}