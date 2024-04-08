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
var x = {
  string: () => ({
    parse(arg) {
      isString(arg);
      return arg;
    }
  }),
  number: () => ({
    parse(arg) {
      isNumber(arg);
      return arg;
    }
  })
};
console.log(`++++++++ Parsers ========`);
var strParser = x.string();
var val0 = strParser.parse("hello");
console.log(val0);
var numParser = x.number();
var val1 = numParser.parse("NaN");
console.log(val1);
