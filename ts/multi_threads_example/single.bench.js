import { bench } from "vitest";
import { promiseCount, singleCount, workerCount } from "./counter.mjs";

const NUM = 100000000;

const numWorkers = 4;

bench("Single thread 100K - 1 counter", async () => {
  await singleCount(NUM);
}, { iterations: 10 });

/*
bench("Single thread 100K - 4 promises", async () => {
  await promiseCount(NUM, numWorkers);
}, { iterations: 10 });
*/

bench("Multi thread 1K - 4 workers", async () => {
  await workerCount(NUM, numWorkers);
}, { iterations: 10 });
