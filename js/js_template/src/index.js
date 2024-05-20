import { abc } from "src/moduleA.js";
import { sum } from "src/moduleB.js";
import { readAndWritePipe } from "./pipe.js";

console.log(`1 + 2 = ${sum(1, 2)}`)

console.log(`Hello there, ${abc}!`);

console.log(`${readAndWritePipe()}`);
