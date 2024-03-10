import { abc } from "src/moduleA.js";
import express from 'express';

import fs from 'fs';
import v8Profiler from 'v8-profiler-next';
const title = 'good-name';

// set generateType 1 to generate new format for cpuprofile
// to be compatible with cpuprofile parsing in vscode.
v8Profiler.setGenerateType(1);

// ex. 5 mins cpu profile
v8Profiler.startProfiling(title, true);
setTimeout(() => {
  const profile = v8Profiler.stopProfiling(title);
  profile.export(function(error, result) {
    // if it doesn't have the extension .cpuprofile then
    // chrome's profiler tool won't like it.
    // examine the profile:
    //   Navigate to chrome://inspect
    //   Click Open dedicated DevTools for Node
    //   Select the profiler tab
    //   Load your file
    fs.writeFileSync(`${title}.cpuprofile`, result);
    profile.delete();
  });
}, 5 * 1000);

const router = express.Router();
const port = 9090;
const app = express();

router.get('/:n', (req, res) => {
  const n = parseInt(req.params.n);
  const result = fib(n);
  // mem();
  res.json({ "result": result });
});

function fib(n: number): number {
  if (n < 2) return 1;
  return fib(n - 1) + fib(n - 2);
}

function mem(n: number) {
  const hugeArray = new Array<Array<string>>(n);

  for (let i = 0; i < hugeArray.length; i++) {
    hugeArray.push(new Array(Math.round(n / 10) + 1).fill('some string')); // Nested arrays
  }
}

app.use(router);

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});


console.log(`Hello there, ${abc}!`);
