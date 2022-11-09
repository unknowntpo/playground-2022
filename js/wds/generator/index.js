function* testFn() {
    yield 1
    yield 2
    yield 3
}

const genFn = testFn()

console.log(genFn.next())

console.log(genFn.next())

console.log(genFn.next())

console.log(genFn.next())
