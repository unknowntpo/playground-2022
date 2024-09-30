import { abc } from "src/moduleA";
import { sum } from "src/moduleB";
import { textGenerate } from "src/openAI";


console.log(`1 + 2 = ${sum(1, 2)}`)

console.log(`Hello there, ${abc}!`);

textGenerate()
