function* testFn() {
    yield 1
    yield 2
    yield 3
}

function* Iter(arr) {
    for (let i = 0; i < arr.length; i++) {
        yield arr[i]
    }
}

// Example 1: basic
const genFn = testFn()

console.log(genFn.next())
console.log(genFn.next())
console.log(genFn.next())
console.log(genFn.next())

// Example 2: iterator
const iter = Iter([1, 3, 5, 7])
console.log(iter.next())
console.log(iter.next())
console.log(iter.next())
console.log(iter.next())

