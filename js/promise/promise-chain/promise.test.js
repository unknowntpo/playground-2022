const promise = () => new Promise((resolve, reject) => {
    const data = { name: "John", age: 13 }
    resolve(data)
})

describe('promise()', () => {
    test('promise.then', () => {
        promise().then((data) => {
            expect(data.name).toEqual("John")
            expect(data.age).toStrictEqual(13)
        })
    })
    test('return new promise from promise.then', () => {
        promise().then((data) => {
            return new Promise((resolve, reject) => {
                const newData = { name: "Alice", age: 14 }
                resolve(newData)
            })
        }).then((data) => {
            expect(data.name).toEqual("Alice")
            expect(data.age).toStrictEqual(14)
        })
    })
})