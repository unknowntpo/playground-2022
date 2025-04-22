import { Worker } from "node:worker_threads";

function countTo(limit) {
  let counter = 0;
  for (let i = 0; i < limit; i++) {
    counter++;
  }
  return counter;
}

export function singleCount(count) {
  for (let i = 0; i < count; i++) {
  }
}

export async function promiseCount(count, numOfPromises) {
  const promises = Array.from(
    { length: numOfPromises },
    () => new Promise((resolve) => resolve(countTo(1000))),
  );
  await Promise.all(promises);
}

export async function workerCount(count, numOfWorkers) {
  const countEachWorker = count / numOfWorkers;
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

  console.log("running with worker threads ...");

  await Promise.all(workerPromises);
}

// workerCount(3000, 4);
