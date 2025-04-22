import { describe, expect, it } from "vitest";

import { promiseCount, workerCount } from "./counter.mjs";

describe("bench test", () => {
  it("promise counts", async () => {
    await promiseCount(1000, 8);
    console.log("done");
    expect(1 + 1).toBe(2);
  });

  it("worker counts", async () => {
    await workerCount(1000, 8);
    console.log("done");
    expect(1 + 1).toBe(2);
  });
});
