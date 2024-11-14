import { abc } from "src/moduleA.js";
import { sum } from "src/moduleB.js";

async function main() {
	console.log(`1 + 2 = ${sum(1, 2)}`)
	console.log(`Hello there, ${abc}!`);
}

main()

