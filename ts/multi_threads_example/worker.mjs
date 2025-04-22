import { parentPort } from "worker_threads";

parentPort.on("message", (count) => {
  for (let i = 0; i < count; i++) {
  }

  parentPort.postMessage("OK");
});
