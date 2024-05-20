import { abc } from "src/moduleA.js";
import { sum } from "src/moduleB.js";
import { Range } from "src/numIterator.js";

console.log(`1 + 2 = ${sum(1, 2)}`)

console.log(`Hello there, ${abc}!`);

const range = new Range(1, 5);

for (let num of range) {
	console.log(num);
}
