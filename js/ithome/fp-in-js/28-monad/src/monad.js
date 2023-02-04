const Right = x => ({
    chain: f => f(x),
    map: f => Right(f(x)),
    fold: (f, g) => g(x),
    toString: () => `Right(${x})`
})

const Left = x => ({
    chain: f => Left(x),
    map: f => Left(x),
    fold: (f, g) => f(x),
    toString: () => `Left(${x})`
})

const fromNullable = x =>
    x != null ? Right(x) : Left(null)

const street = user =>
    fromNullable(user.address)
        .map(address => address.street)
        .fold(() => 'no street', x => x)


describe("monad", () => {
    test("local street", () => {
        expect(
            street(
                // user
                {
                    name: "Bob",
                    address: {
                        street: "local street"
                    },
                },
            ),
        ).toEqual("local street")
    })

    test("no street", () => {
        expect(
            street(
                // user
                {
                    name: "Alice",
                },
            ),
        ).toEqual("no street")
    })
})
