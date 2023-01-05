
function add(x) {
    return (y) => {
        return x + y
    }
}

add_one = add(1)

describe("addOne", () => {
    test("apply add_one to 2", () => {
        input = [1, 2, 3, 4]
        expect(
            add_one(2)
        ).toEqual(3)
    })
})