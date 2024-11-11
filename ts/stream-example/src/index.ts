import { abc } from "src/moduleA";
import { sum } from "src/moduleB";

async function main() {
	console.log(`1 + 2 = ${sum(1, 2)}`)
	console.log(`Hello there, ${abc}!`);
}

main()

