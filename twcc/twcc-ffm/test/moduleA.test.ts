import { describe, test, expect } from 'vitest';

import { abc } from 'src/moduleA';

describe("project tests", () => {
  test("moduleA is correct", async () => {
    expect(abc).toEqual("123");
  });
});
