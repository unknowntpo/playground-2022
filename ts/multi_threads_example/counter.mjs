import { Worker } from "node:worker_threads";
import { benchmarkFunction } from "./benchmarkFunction.mjs";

export function singleCount(count) {
	benchmarkFunction(count);
}

export async function promiseCount(count, numOfPromises) {
	const countEachPromise = Math.ceil(count / numOfPromises);
	const promises = Array.from(
		{ length: numOfPromises },
		() => new Promise((resolve) => resolve(benchmarkFunction(countEachPromise))),
	);
	await Promise.all(promises);
}

export async function workerCount(count, numOfWorkers) {
	const countEachWorker = Math.ceil(count / numOfWorkers);
	const workerPromises = Array.from(
		{ length: numOfWorkers },
		() =>
			new Promise((resolve) => {
				const worker = new Worker("./worker.mjs");

				// 傳遞資料給worker
				worker.postMessage(countEachWorker);
				// 監聽: 接收worker回報的資料
				worker.on("message", (result) => {
					// console.log(`got result: ${result}`);
					resolve();
				});
			}),
	);

	await Promise.all(workerPromises);
}

// workerCount(3000, 4);
