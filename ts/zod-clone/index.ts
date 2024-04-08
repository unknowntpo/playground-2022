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
//
type AssertFn<T> = (arg: unknown) => asserts arg is T;
type ParseFn<T> = (arg: unknown) => T;
type Parser<T> = {
  parse: ParseFn<T>
}

const x = {
  string: () => ({
    parse(arg: unknown): string {
      isString(arg);
      return arg;
    }
  }),
  number: () => ({
    parse(arg: unknown): number {
      isNumber(arg);
      return arg;
    }
  })
}

console.log(`++++++++ Parsers ========`);


const strParser = x.string();
const val0 = strParser.parse("hello");
console.log(val0);

const numParser = x.number();
const val1 = numParser.parse("NaN");
console.log(val1);




