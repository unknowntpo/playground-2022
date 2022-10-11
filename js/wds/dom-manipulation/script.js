const body = document.body
let newNode = document.createElement("div")
newNode.innerText = "hello"
newNode.innerHTML = "<b>Bold</b>"
body.append(newNode)
console.log(body.childNodes) // NodeList [ <p> ]
console.log(newNode)