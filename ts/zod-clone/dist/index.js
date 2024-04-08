"use strict";

// index.ts
console.log("hello");
function isnt(val, typeName) {
  return new Error(`${val} is not a ${typeName}`);
}
function isString(val) {
  if (typeof val !== "string")
    throw isnt(val, "string");
}
function isNumber(val) {
  if (typeof val !== "number")
    throw isnt(val, "number");
}
console.log(`is string: ${isString("hello")}`);
console.log(`is number: ${isNumber(123)}`);
console.log(`is number: ${isNumber("NaN")}`);
