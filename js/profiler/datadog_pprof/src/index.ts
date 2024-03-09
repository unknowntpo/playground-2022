import { abc } from "src/moduleA.js";
import express from 'express';

const router = express.Router();
const port = 9090;
const app = express();

router.get('/:n', (req, res) => {
  const n = parseInt(req.params.n);
  const result = fib(n);
  mem(n);
  res.json({ "result": result });
});

function fib(n: number): number {
  if (n < 2) return 1;
  return fib(n - 1) + fib(n - 2);
}

function mem(n: number) {
  const hugeArray = new Array<Array<string>>(n);

  for (let i = 0; i < hugeArray.length; i++) {
    hugeArray.push(new Array(n / 10).fill('some string')); // Nested arrays
  }
}

app.use(router);

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});


console.log(`Hello there, ${abc}!`);
