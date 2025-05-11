import { parentPort } from "worker_threads";
import { benchmarkFunction } from "./benchmarkFunction.mjs";

parentPort.on("message", (count) => {
	benchmarkFunction(count);
	parentPort.postMessage("OK");
});
