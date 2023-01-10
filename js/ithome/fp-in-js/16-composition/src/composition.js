// Input: str = 'hello world'
// Output: 'HELLO WORLD!'

const pipe = (...fns) => (x) => fns.reduce((v, f) => f(v), x);
const toUpper = (str) => str.toUpperCase()
const addExcl = (str) => str + "!"

/**
 * @param {String} str
 * @return {String} 
 */
const transform1 = function (str) {
    // 請把 code 寫在這
    // toUpper
    // add !
    return pipe(toUpper, addExcl)(str)
    // return "HELLO WORLD!"
};


describe("transfrom1", () => {
    input = 'hello world'
    output = transform1(input)
    test("input is hello world", () => {
        expect(output).toStrictEqual("HELLO WORLD!")
    })
})

// Input: str = 'hello world'
// Output: 'hello world!'
/**
 * @param {String} str
 * @return {String}
 */

const transform2 = function (str) {
    // 請把 code 寫在這

};
