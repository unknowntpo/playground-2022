import { describe, expect, test } from "vitest";

import { callWithOne, readFileAsync, sum } from "../index.js";

describe("basic", () => {
  test("sum from native", () => {
    expect(sum(1, 2)).toBe(3);
  });

  test("read file async", async () => {
    const buf = await readFileAsync("__test__/test.txt");
    console.log(buf.toString());
    expect(1 + 1).toBe(2);
  });

  test("call_with_one", () => {
    const res = callWithOne(100, (i) => i + 1);
    console.log(res);
    expect(res).toBe(101);
  });
});
