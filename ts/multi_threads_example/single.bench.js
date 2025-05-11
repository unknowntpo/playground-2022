import os from "node:os";
import { bench } from "vitest";
import { promiseCount, singleCount, workerCount } from "./counter.mjs";

// 10 ** 10
const NUM = 10 ** 8;

const numWorkers = os.cpus().length;
console.log(numWorkers)

bench("Single thread - 1 counter", () => {
	try {
		singleCount(NUM);
	} catch (error) {
		console.error("Error in single thread counter:", error);
	}
}, { iterations: 10 });

bench(`Single thread - ${numWorkers} promises`, async () => {
	try {
		await promiseCount(NUM, numWorkers);
	} catch (error) {
		console.error("Error in promise counter:", error);
	}
}, { iterations: 10 });

bench(`Multi threads - ${numWorkers} worker threads`, async () => {
	try {
		await workerCount(NUM, numWorkers);
	} catch (error) {
		console.error("Error in worker thread counter:", error);
	}
}, { iterations: 10 });
