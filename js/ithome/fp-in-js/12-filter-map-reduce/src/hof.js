// to do list
var taskList = [
    {
        id: 0,
        title: 'Buy Avocado',
        status: 'pending',
        dueDate: '2020-05-31',
    }, {
        id: 1,
        title: 'Clean house',
        status: 'complete',
        dueDate: '2020-05-21',
    }, {
        id: 2,
        title: 'Implement js Array filter',
        status: 'pending',
        dueDate: '2019-05-21',
    }
];

function Filter(array, fn) {
    let filterTask = []
    array.forEach((e, i) => {
        ifTrue(
            fn(array[i]),
            () => filterTask.push(array[i])
        )
    })
    return filterTask
}

function ifTrue(isTrue, fn) {
    if (isTrue) {
        fn()
    }
}

const filterPending = Filter(taskList, list =>
    list.status === 'pending'
)

const filterIDis1 = Filter(taskList, list =>
    list.id === 1
)




describe("filter status='pending and dueDate = '2019-05-21'", () => {
    test("status='pending'", () => {
        expect(filterPending).toStrictEqual([].concat(taskList[0], taskList[2]))
    })
    test("dueDate = '2019-05-21'", () => {
        expect(filterIDis1).toStrictEqual([].concat(taskList[1]))
    })
})

function Map(array, callback) {
    let out = []

    array.forEach((e) => {
        out.push(callback(e))
    })

    return out
}

describe("Map", () => {
    test("apply x+1 to [1,2,3,4]", () => {
        input = [1, 2, 3, 4]
        expect(
            Map(input, (x) => x + 1)
        ).toStrictEqual([2, 3, 4, 5])
    })
})

const count = Map([1, 2, 3], (x) => x + 1)

console.log(count) // => [2, 3, 4]

module.exports = {
    filterPending,
    filterIDis1,
}