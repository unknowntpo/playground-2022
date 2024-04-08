console.log("hello")

function isnt(val: unknown, typeName: string) {
  return new Error(`${val} is not a ${typeName}`);
}


function isString(val: unknown): asserts val is string {
  if (typeof val !== "string") throw isnt(val, "string");
}

function isNumber(val: unknown): asserts val is number {
  if (typeof val !== "number") throw isnt(val, "number");
}

console.log(`is string: ${isString("hello")}`)
//console.log(`is string: ${isString(123)}`)


console.log(`is number: ${isNumber(123)}`)

// console.log(`is number: ${isNumber("NaN")}`)
