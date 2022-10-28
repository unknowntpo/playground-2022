const obj = {
    "name": "coffee",
    "age": 30,
    "location": "Taiwan"
}

const menu = document.querySelector("#menu")
listProperties(obj)

// https://stackoverflow.com/a/18202926
function listProperties(obj) {
    Object.keys(obj).forEach((k, idx) => {
        let n = document.createElement("ul")
        n.innerText = `${k}: ${obj[k]}`
        menu.appendChild(n)
    })
}

// https://stackoverflow.com/a/16735184
function listProperties_old(obj) {
    for (let p in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, p)) {
            let n = document.createElement("ul")
            n.innerText = `${p}: ${obj[p]}`
            menu.appendChild(n)
        }
    }
}