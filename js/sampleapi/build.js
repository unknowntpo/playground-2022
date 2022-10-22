

const dataStr = '{"k1":[1,2,3],"k2":"str2","k3":{"subK1":"str","subK2":[5,6,6]}}'

const data = JSON.parse(dataStr)

const entries = Object.entries(data)
// console.log(entries)


// entries.forEach((e) => {
//     const key = e[0]
//     const val = e[1]
//     console.log(key)
//     console.log(val)
//     console.log(typeof val)
//     switch (typeof val) {
//         case "string" || "number":
//             console.log(val)
//         case "object":
//     }
// })
let depth = 0

function printObject(obj) {
    if (depth > 3) {
        return
    }
    const entries = Object.entries(obj)

    entries.forEach((e) => {
        const key = e[0]
        const val = e[1]

        console.log(`key: ${key}`)


        if (Array.isArray(val)) {
            console.log("length of array: ", val.length)
            val.forEach((ee) => {
                printObject(ee)
            })

            return
        }

        // console.log(`key: ${key}`)


        switch (typeof val) {
            case "string":
            case "number":
                console.log(`value: ${val}`)
                return
            case "object":
                depth++
                return printObject(val)
            default:
                console.log(`other type: ${typeof val}`)
                return
        }
    })
}

printObject(data)


// data.forEach((e) => {
//     console.log(e)
//     // let ul = document.createElement("ul")
//     // ul.innerText = e.title
//     // menu.appendChild(ul)
// })

