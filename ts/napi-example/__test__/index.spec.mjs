import test from "ava";

import { readFileAsync, sum } from "../index.js";

test("sum from native", (t) => {
  t.is(sum(1, 2), 3);
});

test("read file async", async (t) => {
  const buf = await readFileAsync("__test__/test.txt");
  t.log(buf.toString());
  t.is(1 + 1, 2);
});
